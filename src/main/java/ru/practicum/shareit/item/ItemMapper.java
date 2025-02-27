package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemMapper {
    private final  CommentService commentService;
    private final CommentMapper commentMapper;

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
        List<Comment> comments = commentService.findAllByItemId(item.getId());

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

    public ItemWithBookingDateAndCommentsDto mapToItemWithBookingDateAndCommentsDto(Item item, BookingDtoWithDate last,
                                                                                    BookingDtoWithDate next,
                                                                                    List<CommentDto> commentsDtos) {
        return new ItemWithBookingDateAndCommentsDto(item.getId(), item.getOwner().getId(), item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId(), last, next, commentsDtos);
    }
}
