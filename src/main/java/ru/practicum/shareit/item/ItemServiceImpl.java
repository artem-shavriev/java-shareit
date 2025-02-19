package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользовтель с данным id не найден.");
        }

        Item item =  itemRepository.save(itemMapper.mapToItem(itemDtoRequest));

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDtoUpdate updateItem(ItemDtoUpdate itemDtoRequest, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользовтель с данным id не найден.");
        }

        Item item =  itemRepository.save(itemMapper.mapToItem(itemDtoRequest));

        return itemMapper.mapToItemDtoUpdate(item);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        Item item = itemRepository.getById(itemId);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getOwnerItems(Integer ownerId) {
        List<Item> ownerItems = itemRepository.findAllByOwnerId(ownerId);

        return ownerItems.stream().map(itemMapper::mapToItemDto).toList();
    }

    @Override
    public List<ItemDto> itemSearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            searchItems = itemRepository.search(text);
        }
        return itemMapper.mapToItemDto(searchItems);
    }
}
