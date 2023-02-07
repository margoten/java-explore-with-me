package ru.practicum.main.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService userservice;

    @PostMapping
    public UserDto createUser(@RequestParam() UserDto userDto) {
        return userservice.createUser(userDto);
    }

    @GetMapping()
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userservice.getUsers(users, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@NotNull @PathVariable() long userId) {
        userservice.deleteUser(userId);
    }
}
