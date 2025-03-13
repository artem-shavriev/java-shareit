package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringJUnitConfig
@SpringBootTest
public class ItemServiceImplSpringBootTest {

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Test
    void shouldGetItemById() {
        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Integer itemId = 1;

        Item item = Item.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        Comment comment = Comment.builder().text("text").item(item).author(user).created(LocalDateTime.now()).build();
        List<Comment> commentsList = new ArrayList<>();
        commentsList.add(comment);

        Mockito.when(commentRepository.findAllByItemId(itemId)).thenReturn(commentsList);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));

        ItemDtoWithComments foundItem = itemService.getItem(1);

        Assertions.assertNotNull(foundItem);

        Assertions.assertEquals(foundItem.getName(), item.getName());
        Assertions.assertEquals(foundItem.getRequestId(), item.getRequestId());
    }

    @Test
    void shouldAddItem() {
        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Integer itemId = 1;

        Item item = Item.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        ItemDto itemDto = ItemDto.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        Mockito.when(itemRepository.save(ArgumentMatchers.any())).thenReturn(item);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        ItemDto foundItem = itemService.addItem(itemDto, 1);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(foundItem.getName(), itemDto.getName());
        Assertions.assertEquals(foundItem.getRequestId(), itemDto.getRequestId());
        Assertions.assertEquals(foundItem.getDescription(), itemDto.getDescription());
    }

    @Test
    void shouldUpdateItem() {
        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Integer itemId = 1;

        Item item = Item.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        ItemDtoUpdate itemDtoUpdate = ItemDtoUpdate.builder().name("name1").requestId(1)
                .available(true).description("description1").build();

        Mockito.when(itemRepository.save(ArgumentMatchers.any())).thenReturn(item);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(itemId )).thenReturn(Optional.ofNullable(item));

        ItemDto foundItem = itemService.updateItem(itemDtoUpdate, 1, itemId);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(foundItem.getName(), itemDtoUpdate.getName());
        Assertions.assertEquals(foundItem.getRequestId(), itemDtoUpdate.getRequestId());
        Assertions.assertEquals(foundItem.getDescription(), itemDtoUpdate.getDescription());
    }

    @Test
    void shouldGetOwnerItems() {
        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Integer itemId = 1;

        Item item = Item.builder().id(itemId).name("name1").requestId(1).owner(user).
                available(true).description("description1").build();

        LocalDateTime startBooking = LocalDateTime.now().minusHours(1);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(user).build();

        Comment comment = Comment.builder().text("text").item(item).author(user).created(LocalDateTime.now()).build();

        List<Comment> commentsList = new ArrayList<>();
        commentsList.add(comment);
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(item);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        Mockito.when(itemRepository.save(ArgumentMatchers.any())).thenReturn(item);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findAllByOwnerId(1)).thenReturn(ownerItems);
        Mockito.when(bookingRepository.findAllByItemId(itemId)).thenReturn(bookingList);
        Mockito.when(commentRepository.findAllByItemId(itemId)).thenReturn(commentsList);

        List<ItemWithBookingDateAndCommentsDto> foundItems = itemService.getOwnerItems(1);

        Assertions.assertNotNull(foundItems.get(0));
        Assertions.assertEquals(foundItems.get(0).getName(), item.getName());
        Assertions.assertEquals(foundItems.get(0).getRequestId(), item.getRequestId());
        Assertions.assertEquals(foundItems.get(0).getDescription(), item.getDescription());
        Assertions.assertEquals(foundItems.get(0).getComments().size(), 1);
        Assertions.assertNotNull(foundItems.get(0).getNextBooking());
        Assertions.assertNotNull(foundItems.get(0).getLastBooking());
        Assertions.assertEquals(bookingList.get(0).getStatus(), Status.APPROVED);
    }

    @Test
    void shouldSearchItem() {
        Integer itemId = 1;

        Item item = Item.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Mockito.when(itemRepository.search("description1")).thenReturn(itemList);

        List<ItemDto> foundItems = itemService.itemSearch("description1");

        Assertions.assertNotNull(foundItems);
        Assertions.assertEquals(foundItems.get(0).getName(), item.getName());
        Assertions.assertEquals(foundItems.get(0).getRequestId(), item.getRequestId());
        Assertions.assertEquals(foundItems.get(0).getDescription(), item.getDescription());
    }

    @Test
    void shouldAddComment() {
        Integer itemId = 1;
        Item item = Item.builder().id(itemId).name("name1").requestId(1)
                .available(true).description("description1").build();

        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        CommentDto commentDto = CommentDto.builder().text("text")
                .itemName(item.getName()).authorName(user.getName())
                .created(LocalDateTime.now()).build();

        Comment comment = Comment.builder().text("text").item(item).author(user).created(LocalDateTime.now()).build();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        LocalDateTime startBooking = LocalDateTime.now().minusHours(2);
        LocalDateTime endBooking = LocalDateTime.now().minusHours(1);
        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(user).build();
        List<Booking> userBookings = new ArrayList<>();
        userBookings.add(booking);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findAllByBookerIdOrderByStart(1)).thenReturn(userBookings);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.save(ArgumentMatchers.any())).thenReturn(comment);

        CommentDto savedComment = itemService.addComment(itemId, 1, commentDto);

        Assertions.assertNotNull(savedComment);
        Assertions.assertEquals(savedComment.getText(), commentDto.getText());
        Assertions.assertEquals(savedComment.getItemName(), commentDto.getItemName());
        Assertions.assertEquals(savedComment.getAuthorName(), commentDto.getAuthorName());
    }

    @Test
    void shouldUpdateFields() {
        Item item = Item.builder().id(1).name("name1").requestId(1)
                .available(true).description("description1").build();

        ItemDtoUpdate itemDtoUpdate = ItemDtoUpdate.builder().name("name1").requestId(1)
                .available(true).description("description1").build();

        User user = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Mockito.when(userRepository.getReferenceById(1)).thenReturn(user);

        Item updatedItem = itemService.updateFields(item, itemDtoUpdate);

        Assertions.assertNotNull(updatedItem);
        Assertions.assertEquals(updatedItem.getDescription(), item.getDescription());
        Assertions.assertEquals(updatedItem.getName(), item.getName());
        Assertions.assertEquals(updatedItem.getAvailable(), item.getAvailable());
    }
}
