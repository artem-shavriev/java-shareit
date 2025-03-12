package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get owner items");
        return itemClient.getItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item) {
        return itemClient.addItem(item, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Integer itemId) {
        log.info("Get item {}", itemId);
        return itemClient.findItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Integer itemId, @RequestBody ItemDtoUpdate item,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.updateItem(item, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> itemSearch(@RequestParam String text) {
        return itemClient.itemSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Integer itemId,
                                 @RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @RequestBody @Valid CommentDto text) {
        return itemClient.addComment(itemId, userId, text);
    }

}
