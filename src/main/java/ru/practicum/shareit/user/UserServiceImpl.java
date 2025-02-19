package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDtoRequest) {
        User user = userMapper.mapToUser(userDtoRequest);

        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(userMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto getUserById(Integer userId) {
        return userMapper.mapToUserDto(userRepository.getById(userId));
    }

    @Override
    public UserDto updateUser(UserDto userDtoRequest) {
        User user = userMapper.mapToUser(userDtoRequest);
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto deleteUser(Integer userId) {
        UserDto userDto = getUserById(userId);
        userRepository.deleteById(userId);

       return userDto;
    }
}
