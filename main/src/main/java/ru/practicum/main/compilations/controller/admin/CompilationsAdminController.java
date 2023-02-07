package ru.practicum.main.compilations.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.service.CompilationsService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/compilations")
public class CompilationsAdminController {
    private final CompilationsService compilationsService;

    @PostMapping
    public CompilationDto createCompilation(@RequestParam() NewCompilationDto compilationDto) {

        return compilationsService.createCompilation(compilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable() long compId, @RequestParam() NewCompilationDto compilationDto) {
        return compilationsService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable() long compId) {
        compilationsService.deleteCompilation(compId);
    }
}
