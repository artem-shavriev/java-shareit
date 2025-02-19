package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class UserMapper {
    public UserDto mapToUserDto(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не должен быть равен null");
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(user.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
