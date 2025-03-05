package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Integer requestorId) {
        String userRequestDescription = requestDto.getDescription();

        if (userRequestDescription == null || userRequestDescription.isBlank()) {
            log.error("В запросе некорректное описание вещи.");
            throw  new NotFoundException("В запросе нет описания.");
        }

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(userRequestDescription);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestorId(requestorId);

        itemRequest = requestRepository.save(itemRequest);
        log.info("Создан запрос с описанием: {} пользователем с id {}", userRequestDescription, requestorId);
        return requestMapper.mapToDto(itemRequest);
    }
}
