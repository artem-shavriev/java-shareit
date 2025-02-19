package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMemoryRepository userMemoryRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDtoRequest) {
        return userMapper.mapToUserDto(userMemoryRepository.addUser(userDtoRequest));
    }

    @Override
    public List<UserDto> getUsers() {
        return userMemoryRepository.getUsers().stream().map(userMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto getUserById(Integer userId) {
        return userMapper.mapToUserDto(userMemoryRepository.getUserById(userId));
    }

    @Override
    public UserDto updateUser(UserDto userDtoRequest, Integer userId) {
        return userMapper.mapToUserDto(userMemoryRepository.updateUser(userDtoRequest, userId));
    }

    @Override
    public UserDto deleteUser(Integer userId) {
        return userMapper.mapToUserDto(userMemoryRepository.deleteUser(userId));
    }
}
