package ru.practicum.main.user.service;

import ru.practicum.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    void deleteUser(long userId);

    List<UserDto> getUsers(List<Long> users, int from, int size);
}
