package ru.practicum.main.request.mapper;

import ru.practicum.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus().name())
                .created(request.getCreated())
                .build();
    }

    public static EventRequestStatusUpdateResult toRequestStatusUpdateResultDto(List<Request> requestList) {
        Map<Boolean, List<RequestDto>> requestDtoMap = requestList.stream()
                .map(RequestMapper::toRequestDto)
                .distinct()
                .collect(Collectors.groupingBy(request -> request.getStatus().equals(Request.RequestStatus.CONFIRMED.name())));
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(requestDtoMap.get(true))
                .rejectedRequests(requestDtoMap.get(false))
                .build();
    }
}
