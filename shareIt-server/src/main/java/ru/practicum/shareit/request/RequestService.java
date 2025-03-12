package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
public interface RequestService {
    ItemRequestDto addRequest(ItemRequestDto requestDto, Integer requestorId);

    List<ItemRequestDto> findUserRequests(Integer requestorId);

    List<ItemRequestDto> findAllRequests(Integer requestorId);

    ItemRequestDto findByRequestId(Integer requestId);
}
