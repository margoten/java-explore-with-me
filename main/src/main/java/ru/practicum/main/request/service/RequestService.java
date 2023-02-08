package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> requestService(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getUserEventRequests(Long userId, Long eventId);

    RequestDto updateUserEventRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
