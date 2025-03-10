package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Service
interface ItemService {
    ItemDto addItem(ItemDto itemDtoRequest, Integer userId);

    ItemDto updateItem(ItemDtoUpdate itemDtoRequest, Integer userId, Integer itemId);

    ItemDtoWithComments getItem(Integer itemId);

    List<ItemWithBookingDateAndCommentsDto> getOwnerItems(Integer ownerId);

    List<ItemDto> itemSearch(String text);

    CommentDto addComment(Integer itemId, Integer userId, CommentDto text);
}
