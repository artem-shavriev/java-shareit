package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringJUnitConfig
@Import({UserServiceImpl.class, UserMapper.class})
public class UserServiceImplSpringBootTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUserById_shouldReturnUserDto() {
        Integer userId = 1;
        User user = User.builder().id(userId).name("Add").email("@Add.com").build();

        Mockito.when(userRepository.getById(userId)).thenReturn(user);

        UserDto foundUser = userService.getUserById(userId);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(foundUser.getId(), user.getId());
        Assertions.assertEquals(foundUser.getName(), user.getName());
        Assertions.assertEquals(foundUser.getEmail(), user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).getById(userId);
    }

    @Test
    void getAllUsers_shouldGetAllUsers() {
        User user = User.builder().id(1).name("Add").email("@Add.com").build();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> userDtoList = userService.getUsers();

        Assertions.assertNotNull(userDtoList);
        Assertions.assertEquals(userDtoList.get(0).getId(), user.getId());
        Assertions.assertEquals(userDtoList.get(0).getName(), user.getName());
        Assertions.assertEquals(userDtoList.get(0).getEmail(), user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldUpdateUser() {
        User user = User.builder().id(1).name("Add").email("@Add.com").build();
        UserDto updatedUserDto = UserDto.builder().id(1).name("Update").email("@Update.com").build();
        User updatedUser = User.builder().id(1).name("Update").email("@Update.com").build();

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(updatedUser);

        UserDto update = userService.updateUser(updatedUserDto, 1);

        Assertions.assertNotNull(update);
        Assertions.assertEquals(update.getId(), updatedUser.getId());
        Assertions.assertEquals(update.getName(), updatedUser.getName());
        Assertions.assertEquals(update.getEmail(), updatedUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void shouldUpdateFields() {
        User updatedUser = User.builder().id(1).name("Add").email("@Add.com").build();
        UserDto userDto = UserDto.builder().id(1).name("UpdateAdd").email("Update@Add.com").build();
        UserDto userDto2 = UserDto.builder().id(1).email("Update@Add.com").build();
        UserDto userDto3 = UserDto.builder().id(1).name("UpdateAdd").build();

        User newUser = userService.updateFields(updatedUser, userDto);
        User newUser2 = userService.updateFields(updatedUser, userDto2);
        User newUser3 = userService.updateFields(updatedUser, userDto3);

        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(userDto.getEmail(), newUser.getEmail(), "Имейлы не совпали.");
        Assertions.assertEquals(userDto.getName(), newUser.getName(), "Имена не совпали.");
        Assertions.assertEquals(newUser2.getName(), newUser2.getName(), "Имена не совпали.");
        Assertions.assertEquals(newUser3.getEmail(), newUser3.getEmail(), "Имена не совпали.");

    }
}

