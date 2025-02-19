package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDtoRequest);

    List<UserDto> getUsers();

    UserDto updateUser(UserDto userDtoRequest);

    UserDto getUserById(Integer userId);

    UserDto deleteUser(Integer userId);
}
