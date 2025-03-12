package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

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
    void shouldUpdateFields() {
        User updatedUser = User.builder().id(1).name("Add").email("@Add.com").build();
        UserDto userDto = UserDto.builder().id(1).name("UpdateAdd").email("Update@Add.com").build();

        User newUser = userService.updateFields(updatedUser, userDto);

        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(userDto.getEmail(), newUser.getEmail(), "Имейлы не совпали.");
        Assertions.assertEquals(userDto.getName(), newUser.getName(), "Имена не совпали.");

    }
}

