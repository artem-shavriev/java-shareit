package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAnswerDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public ItemRequestDto mapToDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestorId(itemRequest.getRequestorId());
        itemRequestDto. setCreated(itemRequest.getCreated());

        return itemRequestDto;
    }

    public RequestAnswerDto mapToRequestAnswerDto(Item item) {
        RequestAnswerDto requestAnswerDto = new RequestAnswerDto();

        requestAnswerDto.setItemId(item.getId());
        requestAnswerDto.setName(item.getName());
        requestAnswerDto.setOwnerId(item.getOwner().getId());

        return requestAnswerDto;
    }

    public List<RequestAnswerDto> mapToRequestAnswerDto(List<Item> itemList) {
        return itemList.stream().map(item -> mapToRequestAnswerDto(item)).toList();
    }
}