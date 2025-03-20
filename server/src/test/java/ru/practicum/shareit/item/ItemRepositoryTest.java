package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByOwnerId() {
        User user = User.builder().name("Add").email("@Add.com").build();
        userRepository.save(user);

        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        ItemRequest itemRequest = ItemRequest.builder().description("description").created(create)
                .requestorId(3).build();

        Integer requestId = itemRequest.getId();

        Item item = Item.builder().owner(user).name("name").requestId(requestId)
                .available(true).description("description").build();

        itemRepository.save(item);

        List<Item> itemsList = itemRepository.findAllByOwnerId(user.getId());

        Assertions.assertNotNull(itemsList.get(0));
    }

    @Test
    void shouldSearchByDescription() {
        User user = User.builder().name("Add1").email("@Add1.com").build();
        userRepository.save(user);

        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        ItemRequest itemRequest = ItemRequest.builder().description("description1").created(create)
                .requestorId(3).build();

        Integer requestId = itemRequest.getId();

        Item item = Item.builder().owner(user).name("name1").requestId(requestId)
                .available(true).description("description1").build();

        itemRepository.save(item);

        List<Item> itemsList = itemRepository.search("description1");
        Assertions.assertNotNull(itemsList.get(0));
    }

    @Test
    void shouldFindAllByRequestId() {
        User user = User.builder().name("Add2").email("@Add.com2").build();
        userRepository.save(user);

        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        ItemRequest itemRequest = ItemRequest.builder().description("description2").created(create)
                .requestorId(3).build();

        Integer requestId = itemRequest.getId();

        Item item = Item.builder().owner(user).name("name2").requestId(requestId)
                .available(true).description("description2").build();

        itemRepository.save(item);

        List<Item> itemsList = itemRepository.findAllByRequestId(requestId);
        Assertions.assertNotNull(itemsList.get(0));
    }
}
