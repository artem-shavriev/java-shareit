package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemWithBookingDateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemMapper {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

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

    public ItemDtoUpdate mapToItemDtoUpdate(Item item) {
        ItemDtoUpdate itemDto = new ItemDtoUpdate();

        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setRequestId(item.getRequestId());

        return itemDto;
    }

    public Item mapToItem(ItemDtoUpdate itemDto, User owner, Integer itemId) {
        Item item = new Item();

        item.setId(itemId);
        item.setOwner(owner);
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setRequestId(item.getRequestId());

        return item;
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

    public ItemWithBookingDateDto mapToItemWithBookingDateDto(Item item) {
        String startBooking = "нет брони";
        String endBooking = "нет брони";

        List<String> dateList =  bookingRepository.findItemsBookingDate(item.getId());
        if (dateList.size() > 0) {
            startBooking = dateList.get(0);
            endBooking = dateList.get(1);
        }

        return new ItemWithBookingDateDto(item.getId(), item.getOwner().getId(), item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId(), startBooking, endBooking);
    }

    public List<ItemWithBookingDateDto> mapToItemWithBookingDateDto(List<Item> items) {
        return items.stream().map(item -> mapToItemWithBookingDateDto(item)).toList();
    }
}
