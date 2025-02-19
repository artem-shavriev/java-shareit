package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMemoryRepository itemMemoryRepository;

    @Override
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {
        return itemMemoryRepository.addItem(itemDtoRequest, userId);
    }

    @Override
    public ItemDto updateItem(Integer itemId, ItemDtoUpdate itemDtoRequest, Integer userId) {
        return itemMemoryRepository.updateItem(itemId, itemDtoRequest, userId);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return itemMemoryRepository.getItem(itemId);
    }

    @Override
    public List<ItemDto> getOwnerItems(Integer ownerId) {
        return itemMemoryRepository.getOwnerItems(ownerId);
    }

    @Override
    public List<ItemDto> itemSearch(String text) {
        return itemMemoryRepository.itemSearch(text);
    }
}
