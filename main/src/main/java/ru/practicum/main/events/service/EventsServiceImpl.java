package ru.practicum.main.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.error.ValidationException;
import ru.practicum.main.events.dto.*;
import ru.practicum.main.events.mapper.EventMapper;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.QEvent;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;
import ru.practicum.stats.client.EndpointHitClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventsServiceImpl implements EventsService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EndpointHitClient hitClient;


    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        QEvent event = QEvent.event;
        BooleanExpression textEvents = event.title.toUpperCase().contains(text)
                .or(event.annotation.toUpperCase().contains(text))
                .or(event.description.toUpperCase().contains(text));
        BooleanExpression categoryEvents = event.category.id.in(categories);
        BooleanExpression rangeEvents = rangeStart != null && rangeEnd != null
                ? event.eventDate.between(rangeStart, rangeEnd)
                : event.eventDate.after(LocalDateTime.now());
        BooleanExpression paidEvents = event.paid.eq(paid);
        BooleanExpression res = rangeEvents;
        if (!text.isEmpty()) {
            res = textEvents;
        }
        if (!categories.isEmpty()) {
            res = res.and(categoryEvents);
        }
        if (paid != null) {
            res = res.and(paidEvents);
        }

        return eventRepository.findAll(res, pageRequest)
                .stream()
                .map(EventMapper::toEventShotDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Event with id=" + id + " was not found");
                });
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(long initiatorId, long eventId) {
        Event event = eventRepository.findEventByInitiator_IdAndIdIs(initiatorId, eventId);
        if (event == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequestDto eventRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Event with id=" + eventId + " was not found");
                });
        if (eventRequestDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventRequestDto.getCategory())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Category with id=" + eventRequestDto.getCategory() + " was not found");
                    });
            event.setCategory(category);
        }

        if (eventRequestDto.getEventDate() != null) {
            event.setEventDate(eventRequestDto.getEventDate());
        }

        if (eventRequestDto.getStateAction() == UpdateEventAdminRequestDto.StateAction.PUBLISH_EVENT) {
            if (event.getState() != Event.EventState.PENDING) {
                throw new ValidationException("Cannot publish the event because it's not in the right state: " +  event.getState());
            }
            LocalDateTime published = LocalDateTime.now();
            if (published.minusHours(1).isBefore(event.getEventDate())) {
                throw new ValidationException("Invalid event date for event " + eventId);
            }
            event.setPublished(published);
            event.setState(Event.EventState.PUBLISHED);
        }

        if (eventRequestDto.getStateAction() == UpdateEventAdminRequestDto.StateAction.REJECT_EVENT) {
            if (event.getState() == Event.EventState.PUBLISHED && event.getPublished().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Event with id=" + eventId + " cannot be rejected");
            }
            event.setState(Event.EventState.CANCELED);
        }
        if (eventRequestDto.getAnnotation() != null) {
            event.setAnnotation(eventRequestDto.getAnnotation());
        }
        if (eventRequestDto.getDescription() != null) {
            event.setDescription(eventRequestDto.getDescription());
        }
        if (eventRequestDto.getTitle() != null) {
            event.setTitle(eventRequestDto.getTitle());
        }

        if (eventRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(eventRequestDto.getRequestModeration());
        }
        if (eventRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        }

        if (eventRequestDto.getPaid() != null) {
            event.setPaid(eventRequestDto.getPaid());
        }
        if (eventRequestDto.getLocation() != null) {
            event.setLat(eventRequestDto.getLocation().getLat());
            event.setLon(eventRequestDto.getLocation().getLon());

        }

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        var eventStates = states.isEmpty()
                ? List.of(Event.EventState.values())
                : states.stream().map(Event.EventState::valueOf).collect(Collectors.toList());
        QEvent event = QEvent.event;
        BooleanExpression initiatorEvents = event.initiator.id.in(users);
        BooleanExpression statesEvents = event.state.in(eventStates);
        BooleanExpression categoryEvents = event.category.id.in(categories);
        BooleanExpression rangeEvents = event.eventDate.between(rangeStart, rangeEnd);
        BooleanExpression res = event.isNotNull();
        if (!users.isEmpty()) {
            res = initiatorEvents;
        }
        if (!states.isEmpty()) {
            res = res.and(statesEvents);
        }
        if (!categories.isEmpty()) {
            res = res.and(categoryEvents);
        }
        if (rangeStart != null && rangeEnd != null) {
            res = res.and(rangeEvents);
        }
        return eventRepository.findAll(res, pageRequest)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getUserEvents(Long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiator_IdIs(userId, pageRequest)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        LocalDateTime current = LocalDateTime.now();
        if (eventDto.getEventDate().minusHours(2).isBefore(current)) {
            throw new ValidationException("Invalid event date for event " + eventDto.getTitle());
        }
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("User with id=" + userId + " was not found");
                });
        Event event = EventMapper.toEvent(eventDto);
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> {
                    throw new NotFoundException("Category with id=" + eventDto.getCategory() + " was not found");
                });
        event.setCreated(LocalDateTime.now());
        event.setCategory(category);
        event.setInitiator(initiator);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequestDto eventRequestDto) {
        Event event = eventRepository.findEventByInitiator_IdAndIdIs(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (event.getState() == Event.EventState.PUBLISHED) {
            throw new ValidationException("Event must not be published");
        }

        if (eventRequestDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventRequestDto.getCategory())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Category with id=" + eventRequestDto.getCategory() + " was not found");
                    });
            event.setCategory(category);
        }

        LocalDateTime current = LocalDateTime.now();
        if (eventRequestDto.getEventDate() != null) {
            if (eventRequestDto.getEventDate().minusHours(2).isBefore(current)) {
                throw new ValidationException("Invalid event date for event " + eventId);
            }
            event.setEventDate(eventRequestDto.getEventDate());
        }

        if (eventRequestDto.getStateAction() == UpdateEventUserRequestDto.StateAction.SEND_TO_REVIEW) {
            event.setState(Event.EventState.PENDING);
        }

        if (eventRequestDto.getStateAction() == UpdateEventUserRequestDto.StateAction.CANCEL_REVIEW) {
            event.setState(Event.EventState.CANCELED);
        }
        if (eventRequestDto.getAnnotation() != null) {
            event.setAnnotation(eventRequestDto.getAnnotation());
        }
        if (eventRequestDto.getDescription() != null) {
            event.setDescription(eventRequestDto.getDescription());
        }
        if (eventRequestDto.getTitle() != null) {
            event.setTitle(eventRequestDto.getTitle());
        }

        if (eventRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(eventRequestDto.getRequestModeration());
        }
        if (eventRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        }

        if (eventRequestDto.getPaid() != null) {
            event.setPaid(eventRequestDto.getPaid());
        }
        if (eventRequestDto.getLocation() != null) {
            event.setLat(eventRequestDto.getLocation().getLat());
            event.setLon(eventRequestDto.getLocation().getLon());
        }

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}
