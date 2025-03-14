
package ru.practicum.shareit.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestDataIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldCreateRequest() {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);

        ItemRequestDto itemRequestToCreate = ItemRequestDto.builder().description("description").created(create)
                .build();

        ItemRequestDto createdItemRequest = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(2).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Sharer-User-Id", "2");

        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemRequestToCreate, headers);

        ResponseEntity<ItemRequestDto> response = testRestTemplate.exchange(
                "/requests",
                HttpMethod.POST,
                requestEntity,
                ItemRequestDto.class
        );

        ItemRequestDto actualRequest = response.getBody();
        Assertions.assertNotNull(actualRequest.getId());

        assertThat(actualRequest)
                .usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(createdItemRequest);
    }


@Test
    void shouldFindUserRequests() {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);
        UserDto userDto = UserDto.builder().name("Add").email("@Add.com").build();
        UserDto createdUserDto = testRestTemplate.postForObject("/users", userDto, UserDto.class);

        Integer requestorId = createdUserDto.getId();

        ItemRequestDto itemRequestToCreate = ItemRequestDto.builder().description("description").created(create)
                .build();

        ItemRequestDto createdItemRequest = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(requestorId).build();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("X-Sharer-User-Id", requestorId.toString()); // Добавляем заголовок

        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemRequestToCreate, requestHeaders);

        ResponseEntity<ItemRequestDto> response = testRestTemplate.exchange(
                "/requests",
                HttpMethod.POST,
                requestEntity,
                ItemRequestDto.class
        );

        ItemRequestDto actualRequest = response.getBody();
        Integer requestId = actualRequest.getId();

        ItemDto itemDto = ItemDto.builder().name("name").description("desc")
                .available(true).requestId(requestId).build();

        HttpHeaders itemHeaders = new HttpHeaders();
        itemHeaders.setContentType(MediaType.APPLICATION_JSON);
        itemHeaders.set("X-Sharer-User-Id", requestorId.toString()); // Добавляем заголовок

        HttpEntity<ItemDto> itemEntity = new HttpEntity<>(itemDto, requestHeaders);

        ResponseEntity<ItemDto> itemResponse = testRestTemplate.exchange(
                "/items",
                HttpMethod.POST,
                requestEntity,
                ItemDto.class
        );

        HttpEntity<ItemRequestDto> request = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<ItemRequestDto>> responseRequestList = testRestTemplate.exchange(
                "/requests",
                HttpMethod.GET,
                requestEntity,
                List<ItemRequestDto>.class
        );

        List<ItemRequestDto> actualRequestList = (List<ItemRequestDto>) response.getBody();

        Assertions.assertNotNull(actualRequest.getId());

        assertThat(actualRequest)
                .usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(createdItemRequest);
    }

}

