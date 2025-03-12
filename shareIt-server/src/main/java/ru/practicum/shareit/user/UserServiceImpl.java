package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDtoRequest) {
        User user = userMapper.mapToUser(userDtoRequest);
        user = userRepository.save(user);
        log.info("Пользователь добавлен");
        return userMapper.mapToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {

        log.info("Все пользователи получены.");
        return userRepository.findAll().stream().map(userMapper::mapToUserDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Integer userId) {

        log.info("Пользователь с id {} получен.", userId);
        return userMapper.mapToUserDto(userRepository.getById(userId));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDtoRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным id не существует."));

        user = updateFields(user, userDtoRequest);

        log.info("Пользователь с id {} обновлен.", userId);
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с данным id не существует."));
        userRepository.deleteById(userId);

        log.info("Пользователь с id {} удален.", userId);
       return userMapper.mapToUserDto(user);
    }

    public User updateFields(User user, UserDto userDto) {
        if (userDto.hasName()) {
            user.setName(userDto.getName());
        }
        if (userDto.hasEmail()) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}
