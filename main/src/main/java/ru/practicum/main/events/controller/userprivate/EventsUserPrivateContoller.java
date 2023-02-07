package ru.practicum.main.events.controller.userprivate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.dto.UpdateEventUserRequestDto;
import ru.practicum.main.events.service.EventsService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/users/{userId}/events")
public class EventsUserPrivateContoller {
    private final EventsService eventsService;

    @PostMapping()
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestParam() NewEventDto eventDto) {
        return eventsService.createEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
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


}
