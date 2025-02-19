package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

 interface ItemService {
     ItemDto addItem(ItemDto itemDtoRequest, Integer userId);

     ItemDto updateItem(Integer itemId, ItemDtoUpdate itemDtoRequest, Integer userId);

     ItemDto getItem(Integer itemId);

     List<ItemDto> getOwnerItems(Integer ownerId);

     List<ItemDto> itemSearch(String text);
}
