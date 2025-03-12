package ru.practicum.shareit.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDataIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldCreateUser() {
        UserDto userDto = UserDto.builder().name("Add").email("@Add.com").build();
        UserDto expectedDto = UserDto.builder().name("Add").email("@Add.com").build();

        UserDto actualUserDto = testRestTemplate.postForObject("/users", userDto, UserDto.class);

        Assertions.assertNotNull(actualUserDto.getId());

        assertThat(actualUserDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDto);
    }

    /*@Test
    void shouldUpdateUser() {
        UserDto userDto = UserDto.builder().name("Add5").email("@Add5.com").build();

        // Создаём пользователя
        UserDto createdUserDto = webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .returnResult()
                .getResponseBody();

        Integer userId = createdUserDto.getId();

        // Обновляем пользователя (PATCH)
        UserDto expectedDto = UserDto.builder().id(userId).name("UpdateAdd").email("Update@Add.com").build();

        UserDto updatedUserDto = webTestClient.patch()
                .uri("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(expectedDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedUserDto).isEqualTo(expectedDto);
    }
*/

   /* @Test
    void shouldUpdateUser() {
        UserDto userDto = UserDto.builder().name("Add5").email("@Add5.com").build();

        UserDto createdUserDto = restTemplate.postForObject("http://localhost:" + port + "/users", userDto, UserDto.class);
        Integer userId = createdUserDto.getId();

        UserDto expectedDto = UserDto.builder().id(userId).name("UpdateAdd").email("Update@Add.com").build();

        UserDto updatedUserDto = restTemplate
                .patchForObject("http://localhost:" + port + "/users/{userId}", expectedDto, UserDto.class, userId);

        assertThat(updatedUserDto).isEqualTo(expectedDto);
    }*/

    @Test
    void shouldDeleteUser() {
        UserDto userDto = UserDto.builder().name("Add2").email("@Add2.com").build();
        UserDto userDto2 = UserDto.builder().name("Add3").email("@Add3.com").build();

        UserDto createdUserDto = testRestTemplate.postForObject("/users", userDto, UserDto.class);
        UserDto createdUserDto2 = testRestTemplate.postForObject("/users", userDto2, UserDto.class);
        Integer userId = createdUserDto.getId();
        testRestTemplate.delete("/users/" + userId);
        List<UserDto> findUsers = testRestTemplate.getForObject("/users", List.class);

        assertThat(findUsers.size()).isEqualTo(2);
    }
}
