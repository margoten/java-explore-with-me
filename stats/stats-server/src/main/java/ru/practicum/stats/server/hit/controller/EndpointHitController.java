package ru.practicum.stats.server.hit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.hit.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getEndpointHits(@RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                              @RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                              @RequestParam(required = false) List<String> uris,
                                              @RequestParam(defaultValue = "false") boolean unique) {
        return endpointHitService.getEndpointHits(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndpointHitDto createEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        return endpointHitService.createEndpointHit(endpointHitDto);
    }
}
