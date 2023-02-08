package ru.practicum.main.events.controller.userprivate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.dto.UpdateEventUserRequestDto;
import ru.practicum.main.events.service.EventsService;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/users/{userId}/events")
public class EventsUserPrivateContoller {
    private final EventsService eventsService;
    private final RequestService requestService;

    @PostMapping()
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestParam() NewEventDto eventDto) {
        return eventsService.createEvent(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable() Long userId,
                                    @PathVariable() Long eventId,
                                    @RequestParam() UpdateEventUserRequestDto eventDto) {
        return eventsService.updateEventByInitiator(userId, eventId, eventDto);
    }


    @PatchMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable() Long userId, @PathVariable() Long eventId) {
        return eventsService.getEvent(userId, eventId);
    }

    @GetMapping()
    public List<EventFullDto> getUserEvents(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return eventsService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getUserEventRequests(@PathVariable() Long userId,
                                                 @PathVariable() Long eventId) {
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestDto updateUserEventRequest(@PathVariable() Long userId,
                                             @PathVariable() Long eventId,
                                             @RequestParam() EventRequestStatusUpdateRequest request) {
        return requestService.updateUserEventRequest(userId, eventId, request);
    }

}
