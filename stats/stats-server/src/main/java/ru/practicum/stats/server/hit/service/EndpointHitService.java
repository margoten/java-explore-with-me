package ru.practicum.stats.server.hit.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    List<ViewStatsDto> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    EndpointHitDto createEndpointHit(EndpointHitDto endpointHitDto);
}
