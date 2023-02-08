package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.error.ValidationException;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> requestService(Long userId) {
        return requestRepository.findAllByRequester_IdIs(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Event with id=" + eventId + " was not found");
                });
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("User with id=" + userId + " cannot create request for event with id " + eventId);
        }
        if (event.getState() != Event.EventState.PUBLISHED) {
            throw new ValidationException("User with id=" + userId + " cannot create request for unpublished event with id " + eventId);
        }

        List<Request> requests = requestRepository.findAllByEvent_IdIs(eventId);
        if (requests.stream().anyMatch(r -> r.getRequester().getId().equals(userId))) {
            throw new ValidationException("User with id=" + userId + " already sent request for event with id " + eventId);
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requests.size()) {
            throw new ValidationException("User with id=" + userId + " cannot create request for unpublished event whit id " + eventId);
        }

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("User with id=" + userId + " was not found");
                });

        Request newRequest = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? Request.RequestStatus.PENDING : Request.RequestStatus.CONFIRMED)
                .build();
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findAllByEvent_IdIsAndRequester_IdIs(userId, requestId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Request with id=" + requestId + " was not found");
                });

        request.setStatus(Request.RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        return requestRepository.findAllByEvent_IdIsAndRequester_IdIs(eventId, userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto updateUserEventRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Event with id=" + eventId + " was not found");
                });

        List<Request> requests = requestRepository.findAllByEvent_IdIsAndIdIn(eventId, request.getRequestIds());
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requests.size()) {
            throw new ValidationException("User with id=" + userId + " cannot create request for unpublished event whit id " + eventId);
        }
        return null;
    }
}
