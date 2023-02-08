package ru.practicum.main.request.mapper;

import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.model.Request;

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
}
