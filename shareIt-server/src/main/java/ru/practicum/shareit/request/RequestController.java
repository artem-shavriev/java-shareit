package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.addRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.findUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.findAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findByRequestId(@PathVariable Integer requestId) {
        return requestService.findByRequestId(requestId);
    }
}
