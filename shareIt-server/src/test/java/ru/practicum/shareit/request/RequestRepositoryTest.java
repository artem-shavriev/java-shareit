package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindAllByRequestorId() {
        LocalDateTime create = LocalDateTime.now();

        User user = User.builder().id(1).name("Add").email("@Add.com").build();
        User createdUser = userRepository.save(user);
        Integer userId = createdUser.getId();

        ItemRequest itemRequestToCreate = ItemRequest.builder().created(create).description("description")
                .requestorId(userId).build();
        requestRepository.save(itemRequestToCreate);

        List<ItemRequest> requestsList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(1);

        Assertions.assertNotNull(requestsList.get(0));
        Assertions.assertEquals(requestsList.get(0).getDescription(), "description");
    }

    @Test
    void shouldFindAllOrderByCreatedDesc() {
        List<ItemRequest> requestsList = requestRepository.findAllOrderByCreatedDesc(1);
        Assertions.assertNotNull(requestsList);
    }
}
