package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public ItemRequestDto mapToDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequestorId(), itemRequest.getCreated());
    }

    public List<ItemRequestDto> mapToDto(Iterable<ItemRequest> itemRequests) {

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        for (ItemRequest request : itemRequests) {
            itemRequestDtos.add(mapToDto(request));
        }

        return itemRequestDtos;
    }

    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(),
                itemRequestDto.getRequestor(), itemRequestDto.getCreated());
    }
}