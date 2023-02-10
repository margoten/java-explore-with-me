package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.practicum.main.events.model.Location;

import java.time.LocalDateTime;

@Getter
public class UpdateEventAdminRequestDto {
    private String annotation;
    private String title;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    StateAction stateAction;


    public static enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}
