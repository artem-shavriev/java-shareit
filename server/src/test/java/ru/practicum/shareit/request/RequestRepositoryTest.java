package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
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

        User user = User.builder().name("Add").email("@Add.com").build();
        User createdUser = userRepository.save(user);
        Integer userId = createdUser.getId();

        ItemRequest itemRequestToCreate = ItemRequest.builder().created(create).description("description")
                .requestorId(userId).build();
        requestRepository.save(itemRequestToCreate);

        List<ItemRequest> requestsList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);

        Assertions.assertNotNull(requestsList.get(0));
        Assertions.assertEquals(requestsList.get(0).getDescription(), "description");
    }

    @Test
    void shouldFindAllWithoutRequestorOrderByCreatedDesc() {
        LocalDateTime create = LocalDateTime.now();

        User user1 = User.builder().name("Add2").email("@Add2.com").build();
        User user2 = User.builder().name("Add3").email("@Add3.com").build();
        userRepository.save(user1);
        userRepository.save(user2);

        ItemRequest itemRequestToCreate = ItemRequest.builder().created(create).description("description1")
                .requestorId(user1.getId()).build();
        ItemRequest itemRequestToCreate2 = ItemRequest.builder().created(create).description("description2")
                .requestorId(user2.getId()).build();
        requestRepository.save(itemRequestToCreate);
        requestRepository.save(itemRequestToCreate2);

        List<ItemRequest> expectedRquestsList = requestRepository.findAllByRequestorIdNotOrderByCreatedDesc((user1.getId()));

        Assertions.assertNotNull(expectedRquestsList.get(0));
        Assertions.assertEquals(expectedRquestsList.size(), 1);
        Assertions.assertEquals(expectedRquestsList.get(0).getDescription(), "description2");
    }
}
