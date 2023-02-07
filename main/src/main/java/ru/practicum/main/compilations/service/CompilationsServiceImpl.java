package ru.practicum.main.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.mapper.CompilationMapper;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.compilations.repository.CompilationsRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        return compilationsRepository.findAllByPinnedIs(pinned, pageRequest)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Compilation with id=" + compId + " was not found");
                });
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        Set<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        compilation.setEvents(events);
        return CompilationMapper.toCompilationDto(compilationsRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationsRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
        compilationsRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(long compId, NewCompilationDto compilationDto) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Compilation with id=" + compId + " was not found");
                });
        if(compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if(compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }

        if(compilationDto.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
            compilation.setEvents(events);
        }
        return CompilationMapper.toCompilationDto(compilationsRepository.save(compilation));

    }
}
