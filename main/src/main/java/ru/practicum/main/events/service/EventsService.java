package ru.practicum.main.events.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.events.dto.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventsService {
    List<EventShortDto> getEventsByAdmin(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getEvent(long id);

    EventFullDto getEvent(long initiatorId, long eventId);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequestDto eventUpdate);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventFullDto> getUserEvents(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequestDto eventDto);
}
