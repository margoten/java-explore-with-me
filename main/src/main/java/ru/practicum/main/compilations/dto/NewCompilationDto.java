package ru.practicum.main.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.main.validation.Created;
import ru.practicum.main.validation.Updated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotNull(groups = Created.class)
    private Set<Long> events;
    @NotNull(groups = Created.class)
    private Boolean pinned;
    @NotBlank(groups = Created.class)
    String title;
}
