package ru.practicum.main.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserShotDto {
    private Long id;
    private String name;
}
