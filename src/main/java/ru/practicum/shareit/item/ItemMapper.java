package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setRequest(item.getRequest());

        return itemDto;
    }

    public Item mapToItem(ItemDto itemDto) {
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setRequest(item.getRequest());

        return item;
    }
}

