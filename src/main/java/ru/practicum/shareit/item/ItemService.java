package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemWithBookingDateDto;

import java.util.List;

@Service
interface ItemService {
    ItemDto addItem(ItemDto itemDtoRequest, Integer userId);

    ItemDto updateItem(ItemDtoUpdate itemDtoRequest, Integer userId, Integer itemId);

    ItemDto getItem(Integer itemId);

    List<ItemWithBookingDateDto> getOwnerItems(Integer ownerId);

    List<ItemDto> itemSearch(String text);
}
