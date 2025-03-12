package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.save(itemMapper.mapToItem(itemDtoRequest, user));

        log.info("Добавлена вещь с id {}", item.getId());
        return itemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDtoUpdate itemDtoRequest, Integer userId, Integer itemId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        item = updateFields(item, itemDtoRequest);

        log.info("Вещь с id {} обновлена", itemId);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoWithComments getItem(Integer itemId) {
        Item item = itemRepository.getById(itemId);

        log.info("Вещь с id {} получена.", itemId);
        return itemMapper.mapToItemDtoWithComments(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingDateAndCommentsDto> getOwnerItems(Integer ownerId) {
        List<Item> ownerItems = itemRepository.findAllByOwnerId(ownerId);

        List<ItemWithBookingDateAndCommentsDto> ownerItemsWithDateAndComments = new ArrayList<>();
        ownerItems.forEach(item -> {

            Booking lastBooking;
            Booking nextBooking;
            BookingDtoWithDate last;
            BookingDtoWithDate next;

            List<Booking> bookingList = bookingRepository.findAllByItemId(item.getId());

            if (bookingList.size() > 0 && bookingList != null) {

                LocalDateTime currentEnd = bookingList.get(0).getStart();
                LocalDateTime currentStart = bookingList.get(0).getStart();
                lastBooking = bookingList.get(0);
                nextBooking = bookingList.get(0);

                for (int i = 1; i < bookingList.size(); i++) {

                    if (bookingList.get(i).getStatus() == Status.APPROVED) {

                        if (bookingList.get(i).getEnd().isAfter(currentEnd) &&
                                bookingList.get(i).getEnd().isBefore(LocalDateTime.now())) {
                            currentEnd = bookingList.get(i).getStart();
                            lastBooking = bookingList.get(i);
                        }
                    }

                    if (bookingList.get(i).getStatus() == Status.APPROVED) {

                        if (bookingList.get(i).getStart().isBefore(currentStart) &&
                                bookingList.get(i).getStart().isAfter(LocalDateTime.now())) {
                            currentStart = bookingList.get(i).getStart();
                            nextBooking = bookingList.get(i);
                        }
                    }
                }
                last = bookingMapper.mapToBookingDtoWithDate(lastBooking);
                next = bookingMapper.mapToBookingDtoWithDate(nextBooking);
            } else {
                last = null;
                next = null;
            }

            List<Comment> comments = commentRepository.findAllByItemId(item.getId());

            List<CommentDto> commentsDtos = comments.stream()
                    .map(comment -> commentMapper.mapToCommentDto(comment)).toList();

            ownerItemsWithDateAndComments
                    .add(itemMapper.mapToItemWithBookingDateAndCommentsDto(item, last, next, commentsDtos));
        });

        log.info("Все вещи пользователя с id {} получены", ownerId);
        return ownerItemsWithDateAndComments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> itemSearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            searchItems = itemRepository.search(text);
        }

        log.info("Вещи найдены по тексту в описании или названии.");
        return itemMapper.mapToItemDto(searchItems);
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto) {
        LocalDateTime now = LocalDateTime.now();
        Comment comment = commentMapper.mapToComment(commentDto);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        List<Booking> userBookings = bookingRepository.findAllByBookerIdOrderByStart(userId);

        if (userBookings.isEmpty()) {
            log.error("Пользователь с id {} еще не создавал бронь на вещь с id {}", userId, itemId);
            throw new NotFoundException("Пользователь еще не создавал бронь.");
        }

        List<Booking> currentBooking = new ArrayList<>();
        userBookings.forEach(booking -> {
            if (booking.getItem().getId().equals(itemId) && booking.getEnd().isBefore(now)) {
                currentBooking.add(booking);
            }
        });

        if (currentBooking.isEmpty()) {
            log.error("Пользователь c id {} не может оставить отзыв на предмет c id {}", userId, itemId);
            throw new ValidationException("Пользоветль не может оставить отзыв на этот предмет c id " + itemId);
        }

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        log.info("Отзыв на вещь с id {} оставлен пользователем с id {}", itemId, userId);
        return commentMapper.mapToCommentDto(comment);
    }

    public Item updateFields(Item item, ItemDtoUpdate itemDtoUpdate) {
        if (itemDtoUpdate.hasOwnerId()) {
            item.setOwner(userRepository.getById(itemDtoUpdate.getOwnerId()));
        }

        if (itemDtoUpdate.hasName()) {
            item.setName(itemDtoUpdate.getName());
        }

        if (itemDtoUpdate.hasAvailable()) {
            item.setAvailable(itemDtoUpdate.getAvailable());
        }

        if (itemDtoUpdate.hasDescription()) {
            item.setDescription(itemDtoUpdate.getDescription());
        }

        if (itemDtoUpdate.hasRequestId()) {
            item.setRequestId(itemDtoUpdate.getRequestId());
        }

        return item;
    }
}
