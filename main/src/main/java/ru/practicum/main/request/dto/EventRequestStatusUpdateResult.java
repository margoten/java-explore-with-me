package ru.practicum.main.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EventRequestStatusUpdateResult {
    private Set<RequestDto> confirmedRequests;
    private Set<RequestDto> rejectedRequests;

}
