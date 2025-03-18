package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUsers(String path) {
        return get(path);
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> findUserById(Integer userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto, Integer userId) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUser(Integer userId) {
        return delete("/" + userId);
    }
}
