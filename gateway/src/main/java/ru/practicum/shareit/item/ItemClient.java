package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> addItem(ItemDto item, Integer userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> findItemById(Integer itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> updateItem(ItemDtoUpdate item, Integer itemId, Integer userId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> itemSearch(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", parameters);
    }

    public ResponseEntity<Object> addComment(Integer itemId, Integer userId, CommentDto text) {
        return post("/" + itemId + "/comment", userId, text);
    }
}