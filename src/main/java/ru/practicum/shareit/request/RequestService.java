package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public interface RequestService {
    ItemRequestDto addRequest(ItemRequestDto requestDto, Integer requestorId);

}
