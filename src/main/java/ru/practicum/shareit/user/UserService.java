package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDtoRequest);

    List<UserDto> getUsers();

    UserDto updateUser(UserDto userDtoRequest, Integer userId);

    UserDto getUserById(Integer userId);

    UserDto deleteUser(Integer userId);
}
