package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void addUser_shouldAddUserAndReturnOk() throws Exception {
        UserDto userToCreate = UserDto.builder().name("Add").email("@Add.com").build();
        UserDto userCreated = UserDto.builder().id(1).name("Add").email("@Add.com").build();

        when(userService.addUser(userToCreate)).thenReturn(userCreated);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userToCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userCreated.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userCreated.getEmail()));
    }

    @Test
    void getUsers_shouldGetUsersAndReturnOk() throws Exception {
        UserDto user1 = UserDto.builder().id(1).name("Add").email("@Add.com").build();
        UserDto user2 = UserDto.builder().id(2).name("Add2").email("@Add2.com").build();

        List<UserDto> usersList = new ArrayList<>();
        usersList.add(user1);
        usersList.add(user2);

        when(userService.getUsers()).thenReturn(usersList);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..[0].id").value(user1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..[0].name").value(user1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..[0].email").value(user1.getEmail()))

                .andExpect(MockMvcResultMatchers.jsonPath("$..[1].id").value(user2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..[1].name").value(user2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..[1].email").value(user2.getEmail()));
    }

    @Test
    void getUserById_shouldGetUserByIdAndReturnOk() throws Exception {
        UserDto user2 = UserDto.builder().id(2).name("Add2").email("@Add2.com").build();

        when(userService.getUserById(2)).thenReturn(user2);

        mvc.perform(get("/users/2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user2.getEmail()));
    }

    @Test
    void updateUser_shouldUpdateUserAndReturnOk() throws Exception {
        UserDto userToUpdate = UserDto.builder().id(2).name("Add2").email("@Add2.com").build();

        when(userService.updateUser(userToUpdate, 2)).thenReturn(userToUpdate);

        mvc.perform(patch("/users/2")
                        .content(mapper.writeValueAsString(userToUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userToUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userToUpdate.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userToUpdate.getEmail()));
    }

    @Test
    void deleteUser_shouldDeleteUserAndReturnOk() throws Exception {
        UserDto userToUpdate = UserDto.builder().id(2).name("Add2").email("@Add2.com").build();

        when(userService.deleteUser(2)).thenReturn(userToUpdate);

        mvc.perform(delete("/users/2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userToUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userToUpdate.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userToUpdate.getEmail()));
    }
}