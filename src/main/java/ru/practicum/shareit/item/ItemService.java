package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

@Service
interface ItemService {
    ItemDto addItem(ItemDto itemDtoRequest, Integer userId);

    ItemDtoUpdate updateItem(ItemDtoUpdate itemDtoRequest, Integer userId);

    ItemDto getItem(Integer itemId);

    List<ItemDto> getOwnerItems(Integer ownerId);

    List<ItemDto> itemSearch(String text);
}
