package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAnswerDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Integer requestorId) {
        String userRequestDescription = requestDto.getDescription();

        if (userRequestDescription == null || userRequestDescription.isBlank()) {
            log.error("В запросе некорректное описание вещи.");
            throw new NotFoundException("В запросе нет описания.");
        }

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(userRequestDescription);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestorId(requestorId);

        itemRequest = requestRepository.save(itemRequest);
        log.info("Создан запрос с описанием: {} пользователем с id {}", userRequestDescription, requestorId);
        return requestMapper.mapToDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findUserRequests(Integer requestorId) {
        List<ItemRequest> requestsList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        List<ItemRequestDto> requestsDtoList = new ArrayList<>();

        if (requestsList.isEmpty() || requestsList == null) {
            log.error("Список запросов пуст или не найден.");
            throw new NotFoundException("Список запросов пуст или не найден.");
        }

        for (ItemRequest request : requestsList) {
            Integer requestId = request.getId();

            List<Item> findItemslist = itemRepository.findAllByRequestId(requestId);
            List<RequestAnswerDto> answerList = requestMapper.mapToRequestAnswerDto(findItemslist);

            ItemRequestDto itemRequestDto = requestMapper.mapToDto(request);
            itemRequestDto.setItems(answerList);

            requestsDtoList.add(itemRequestDto);
        }

        log.info("Список запросов пользователя с id {} найден.", requestorId);
        return requestsDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findAllRequests(Integer requestorId) {
        List<ItemRequest> requestsList = requestRepository.findAllOrderByCreatedDesc(requestorId);
        List<ItemRequestDto> requestsDtoList = new ArrayList<>();

        if (requestsList.isEmpty() || requestsList == null) {
            log.error("Список запросов пуст или не найден.");
            throw new NotFoundException("Список запросов пуст или не найден.");
        }

        for (ItemRequest request : requestsList) {
            Integer requestId = request.getId();

            List<Item> findItemslist = itemRepository.findAllByRequestId(requestId);
            List<RequestAnswerDto> answerList = requestMapper.mapToRequestAnswerDto(findItemslist);

            ItemRequestDto itemRequestDto = requestMapper.mapToDto(request);
            itemRequestDto.setItems(answerList);

            requestsDtoList.add(itemRequestDto);
        }

        log.info("Список всех запросов пользователей, кроме запросов ищущего, найден.");
        return requestsDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findByRequestId(Integer requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден по id"));

        List<Item> findItemslist = itemRepository.findAllByRequestId(requestId);
        List<RequestAnswerDto> answerList = requestMapper.mapToRequestAnswerDto(findItemslist);

        ItemRequestDto itemRequestDto = requestMapper.mapToDto(request);
        itemRequestDto.setItems(answerList);

        log.info("Запрос по id {} найден", requestId);
        return itemRequestDto;
    }
}
