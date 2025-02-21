package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemWithBookingDateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item =  itemRepository.save(itemMapper.mapToItem(itemDtoRequest, user));

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDtoUpdate updateItem(ItemDtoUpdate itemDtoRequest, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item =  itemRepository.save(itemMapper.mapToItem(itemDtoRequest, user));

        return itemMapper.mapToItemDtoUpdate(item);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        Item item = itemRepository.getById(itemId);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemWithBookingDateDto> getOwnerItems(Integer ownerId) {
        List<Item> ownerItems = itemRepository.findAllByOwnerId(ownerId);

        return itemMapper.mapToItemWithBookingDateDto(ownerItems);
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
