package ru.practicum.main.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.events.model.Location;
import ru.practicum.main.user.dto.UserShotDto;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private long confirmedRequests;
    private LocalDateTime eventDate;
    private UserShotDto initiator;
    private boolean paid;
    private String title;
    private long views;
    private LocalDateTime createdOn;
    private String description;
    private Location location;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private String state;//enum

}
