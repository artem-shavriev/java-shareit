package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
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

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemMapper {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setRequestId(item.getRequestId());

        return itemDto;
    }

    public List<ItemDto> mapToItemDto(List<Item> items) {
        return items.stream().map(item -> mapToItemDto(item)).toList();
    }

    public ItemDtoWithComments mapToItemDtoWithComments(Item item) {
        List<Comment> comments = commentRepository.findAllComment(item.getId());

        List<CommentDto> commentsDtos = comments.stream()
                .map(comment -> commentMapper.mapToCommentDto(comment)).toList();

        ItemDtoWithComments itemDto = new ItemDtoWithComments();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setRequestId(item.getRequestId());
        itemDto.setComments(commentsDtos);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);

        return itemDto;
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

    public Item mapToItem(ItemDto itemDto, User owner) {
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setOwner(owner);
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setRequestId(item.getRequestId());

        return item;
    }

    public ItemWithBookingDateAndCommentsDto mapToItemWithBookingDateAndCommentsDto(Item item) {

        Booking lastBooking;
        Booking nextBooking;
        BookingDtoWithDate last;
        BookingDtoWithDate next;

        List<Booking> bookingList = bookingRepository.findItemsBooking(item.getId());

        if (bookingList.size() > 0 && bookingList != null) {

            Instant currentEnd = bookingList.get(0).getStart();
            Instant currentStart = bookingList.get(0).getStart();
            lastBooking = bookingList.get(0);
            nextBooking = bookingList.get(0);

            for (int i = 1; i < bookingList.size(); i++) {

                if (bookingList.get(i).getStatus() == Status.APPROVED) {

                    if (bookingList.get(i).getEnd().isAfter(currentEnd) &&
                            bookingList.get(i).getEnd().isBefore(Instant.now())) {
                        currentEnd = bookingList.get(i).getStart();
                        lastBooking = bookingList.get(i);
                    }
                }

                if (bookingList.get(i).getStatus() == Status.APPROVED) {

                    if (bookingList.get(i).getStart().isBefore(currentStart) &&
                            bookingList.get(i).getStart().isAfter(Instant.now())) {
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

        List<Comment> comments = commentRepository.findAllComment(item.getId());

        List<CommentDto> commentsDtos = comments.stream()
                .map(comment -> commentMapper.mapToCommentDto(comment)).toList();

        return new ItemWithBookingDateAndCommentsDto(item.getId(), item.getOwner().getId(), item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId(), last, next, commentsDtos);
    }

    public List<ItemWithBookingDateAndCommentsDto> mapToItemWithBookingDateAndCommentsDto(List<Item> items) {
        return items.stream().map(item -> mapToItemWithBookingDateAndCommentsDto(item)).toList();
    }
}
