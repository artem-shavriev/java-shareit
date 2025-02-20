package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public ItemRequestDto mapToDto(ItemRequest itemRequest) {
        String itemRequestCreateDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(itemRequest.getCreated());

        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequestorId(), itemRequestCreateDate);
    }

    public List<ItemRequestDto> mapToDto(Iterable<ItemRequest> itemRequests) {

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        for (ItemRequest request : itemRequests) {
            itemRequestDtos.add(mapToDto(request));
        }

        return itemRequestDtos;
    }

    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(itemRequestDto.getCreated(), dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);
        Instant createDate = zonedDateTime.toInstant();

        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(),
                itemRequestDto.getRequestor(), createDate);
    }
}