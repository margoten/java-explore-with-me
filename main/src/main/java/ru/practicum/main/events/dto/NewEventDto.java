package ru.practicum.main.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.main.events.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(max = 2000, min = 20)
    private final String annotation;
    @NotBlank
    @Size(max = 120, min = 3)
    private final String title;
    @NotNull
    private final Long category;
    @Size(max = 7000, min = 20)
    private final String description;
    @Future
    @NotNull
    private final LocalDateTime eventDate;
    @NotNull
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
}
