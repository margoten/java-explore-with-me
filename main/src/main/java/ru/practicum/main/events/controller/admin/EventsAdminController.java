package ru.practicum.main.events.controller.admin;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequestDto;
import ru.practicum.main.events.service.EventsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/events")
public class EventsAdminController {
    private final EventsService eventsService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable() long eventId,
                                    @RequestBody() UpdateEventAdminRequestDto adminRequestDto) {
        return eventsService.updateEventByAdmin(eventId, adminRequestDto);
    }

    @GetMapping()
    public List<EventFullDto> getEvents(@RequestParam(defaultValue = "") List<Long> users,
                                        @UniqueElements @RequestParam(defaultValue = "") List<String> states,
                                        @UniqueElements @RequestParam(defaultValue = "") List<Long> categories,
                                        @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return eventsService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
