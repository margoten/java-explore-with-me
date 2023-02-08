package ru.practicum.main.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.service.RequestService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getUserRequests(@NotNull @PathVariable() Long userId) {

        return requestService.requestService(userId);
    }

    @PostMapping
    public RequestDto createRequests(@NotNull @PathVariable() Long userId,
                                     @NotNull @RequestParam() Long eventId) {

        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@NotNull @PathVariable() Long userId,
                                    @NotNull @PathVariable() Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

}
