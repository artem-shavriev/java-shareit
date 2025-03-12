package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;

    @Test
    void shouldFindAllByRequestorId() {
        List<ItemRequest> requestsList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(1);
        Assertions.assertNotNull(requestsList);
    }

    @Test
    void shouldFindAllOrderByCreatedDesc() {
        List<ItemRequest> requestsList = requestRepository.findAllOrderByCreatedDesc(1);
        Assertions.assertNotNull(requestsList);
    }
}
