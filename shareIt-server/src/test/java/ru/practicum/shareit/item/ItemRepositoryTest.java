package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAllByOwnerId() {
        List<Item> itemsList = itemRepository.findAllByOwnerId(1);
        Assertions.assertNotNull(itemsList);
    }

    @Test
    void shouldSearchByDescription() {
        List<Item> itemsList = itemRepository.search("text");
        Assertions.assertNotNull(itemsList);
    }

    @Test
    void shouldFindAllByRequestId() {
        List<Item> itemsList = itemRepository.findAllByRequestId(1);
        Assertions.assertNotNull(itemsList);
    }
}
