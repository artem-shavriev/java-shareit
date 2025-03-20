package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
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

    @Test
    void shouldDeleteUser() {
        UserDto userDto = UserDto.builder().name("Add2").email("@Add2.com").build();

        UserDto createdUserDto = testRestTemplate.postForObject("/users", userDto, UserDto.class);
        Integer userId = createdUserDto.getId();
        testRestTemplate.delete("/users/" + userId);
        List<UserDto> findUsers = testRestTemplate.getForObject("/users", List.class);

        assertThat(findUsers.size()).isEqualTo(1);
    }
}
