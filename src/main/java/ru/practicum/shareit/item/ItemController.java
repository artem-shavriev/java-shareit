package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Integer itemId, @RequestBody ItemDtoUpdate item,
                              @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.updateItem(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestParam String text) {
        return itemService.itemSearch(text);
    }
}
