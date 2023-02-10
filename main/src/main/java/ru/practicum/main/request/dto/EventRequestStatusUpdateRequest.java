package ru.practicum.main.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private String status;
}
