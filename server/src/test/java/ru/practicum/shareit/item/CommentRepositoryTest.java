package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindAllByItemId() {
        User user = User.builder().name("Add1").email("@Add1.com").build();
        userRepository.save(user);

        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        ItemRequest itemRequest = ItemRequest.builder().description("description1").created(create)
                .requestorId(3).build();

        Integer requestId = itemRequest.getId();

        Item item = Item.builder().owner(user).name("name1").requestId(requestId)
                .available(true).description("description1").build();

        itemRepository.save(item);

        Comment comment = Comment.builder().author(user).text("text").item(item).created(create).build();
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        Assertions.assertNotNull(comments.get(0));
    }
}
