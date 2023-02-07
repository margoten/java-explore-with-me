package ru.practicum.main.user.mapper;

import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.UserShotDto;
import ru.practicum.main.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static UserShotDto toUserShortDto(User user) {
        return new UserShotDto(
                user.getId(),
                user.getName());
    }

    public static User toUser(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
