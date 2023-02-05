package ru.practicum.stats.server.hit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.hit.jpa.EndpointHitRepository;
import ru.practicum.stats.server.hit.mapper.EndpointHitMapper;
import ru.practicum.stats.server.hit.mapper.ViewStatsMapper;
import ru.practicum.stats.server.hit.model.EndpointHit;
import ru.practicum.stats.server.hit.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public List<ViewStatsDto> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> hits;
        if (unique) {
            hits = endpointHitRepository.getDistinctEndpointHits(start, end, uris);
        } else {
            hits = endpointHitRepository.getEndpointHits(start, end, uris);
        }
        return hits.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    @Override
    public EndpointHitDto createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        return EndpointHitMapper.toEndpointHitDto(endpointHitRepository.save(endpointHit));
    }
}
