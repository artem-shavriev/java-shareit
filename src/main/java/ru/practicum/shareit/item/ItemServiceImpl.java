package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item =  itemRepository.save(itemMapper.mapToItem(itemDtoRequest, user));

        log.info("Добавлена вещь с id {}", item.getId());
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDtoUpdate itemDtoRequest, Integer userId, Integer itemId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item =  itemRepository.getById(itemId);

        item = itemMapper.updateFields(item, itemDtoRequest);

        log.info("Вещь с id {} обновлена", itemId);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDtoWithComments getItem(Integer itemId) {
        Item item = itemRepository.getById(itemId);

        log.info("Вещь с id {} получена.", itemId);
        return itemMapper.mapToItemDtoWithComments(item);
    }

    @Override
    public List<ItemWithBookingDateAndCommentsDto> getOwnerItems(Integer ownerId) {
        List<Item> ownerItems = itemRepository.findAllByOwnerId(ownerId);

        log.info("Все вещи пользователя с id {} получены", ownerId);
        return itemMapper.mapToItemWithBookingDateAndCommentsDto(ownerItems);
    }

    @Override
    public List<ItemDto> itemSearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            searchItems = itemRepository.search(text);
        }

        log.info("Вещи найдены по тексту в описании или названии.");
        return itemMapper.mapToItemDto(searchItems);
    }

    @Override
    public CommentDto addComment(Integer itemId, Integer userId, Comment comment) {
        int threeHoursInSeconds = 10800;
        Instant now = Instant.now().plusSeconds(threeHoursInSeconds);//LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        List<Booking> userBookings = bookingRepository.findAllByBookerId(userId);

        if (userBookings.isEmpty()) {
            log.error("Пользователь с id {} еще не создавал бронь на вещь с id {}", userId, itemId);
            throw new NotFoundException("Пользователь еще не создавал бронь.");
        }

            userBookings.forEach(booking -> {

                if (booking.getBooker().getId().equals(userId) && booking.getEnd().isBefore(now)) {
                    User author = userRepository.findById(userId)
                            .orElseThrow(() -> new NotFoundException("Author not found"));

                    comment.setItem(item);
                    comment.setAuthor(author);
                    comment.setCreated(Instant.now());

                    commentRepository.save(comment);
                }
            });

        log.info("Отзыв на вещь с id {} оставлен пользователем с id {}", itemId, userId);
        return commentMapper.mapToCommentDto(comment);
    }
}
