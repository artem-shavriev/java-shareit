package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig
@SpringBootTest
public class RequestServiceImplTest {
    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private RequestServiceImpl requestService;


    @Test
    void shouldAddItem() {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);

        ItemRequest itemRequestToCreate = ItemRequest.builder().id(1).description("description").created(create)
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("description").created(create)
                .build();

        Mockito.when(requestRepository.save(ArgumentMatchers.any())).thenReturn(itemRequestToCreate);

        ItemRequestDto createdRequest = requestService.addRequest(itemRequestDto, 1);

        Assertions.assertNotNull(createdRequest);
        Assertions.assertEquals(createdRequest.getDescription(), itemRequestToCreate.getDescription());
        Assertions.assertEquals(createdRequest.getRequestorId(), itemRequestToCreate.getRequestorId());
    }

    @Test
    void shouldFindUsersRequests() {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);

        ItemRequest itemRequest = ItemRequest.builder().id(1).description("description").created(create)
                .build();

        List<ItemRequest> requestsList = new ArrayList<>();
        requestsList.add(itemRequest);

        User owner = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Item item = Item.builder().id(1).name("name1").owner(owner).requestId(1)
                .available(true).description("description1").build();

        List<Item> findItemslist = new ArrayList<>();
        findItemslist.add(item);

        Integer requestorId = 1;

        Mockito.when(requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId)).thenReturn(requestsList);
        Mockito.when(itemRepository.findAllByRequestId(requestorId)).thenReturn(findItemslist);

        List<ItemRequestDto> findRequests = requestService.findUserRequests(requestorId);

        Assertions.assertNotNull(findRequests);
        Assertions.assertEquals(findRequests.size(), 1);
        Assertions.assertEquals(findRequests.get(0).getDescription(), itemRequest.getDescription());

        List<ItemRequest> emptyRequestsList = new ArrayList<>();

        Mockito.when(requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId))
                .thenReturn(emptyRequestsList);

        assertThrows(NotFoundException.class, () -> {
            requestService.findUserRequests(requestorId);
        });
    }

    @Test
    void shouldFindAllRequests() {
        Integer requestorId = 1;
        Integer searchRequestorId = 2;
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1).description("description").requestorId(requestorId)
                .created(create).build();

        List<ItemRequest> requestsList = new ArrayList<>();
        requestsList.add(itemRequest);

        User owner = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Item item = Item.builder().id(1).name("name1").owner(owner).requestId(1)
                .available(true).description("description1").build();

        List<Item> findItemslist = new ArrayList<>();
        findItemslist.add(item);

        Mockito.when(requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(searchRequestorId))
                .thenReturn(requestsList);
        Mockito.when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(findItemslist);

        List<ItemRequestDto> findRequests = requestService.findAllRequests(searchRequestorId);

        Assertions.assertNotNull(findRequests);
        Assertions.assertEquals(findRequests.size(), 1);
        Assertions.assertEquals(findRequests.get(0).getDescription(), itemRequest.getDescription());

        List<ItemRequest> emptyRequestsList = new ArrayList<>();

        Mockito.when(requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(searchRequestorId))
                .thenReturn(emptyRequestsList);

        assertThrows(NotFoundException.class, () -> {
            requestService.findAllRequests(searchRequestorId);
        });
    }

    @Test
    void shouldFindByRequestId() {
        Integer requestorId = 1;
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0);

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1).description("description").requestorId(requestorId)
                .created(create).build();

        List<ItemRequest> requestsList = new ArrayList<>();
        requestsList.add(itemRequest);

        User owner = User.builder().id(1).name("Add1").email("@Add1.com").build();

        Item item = Item.builder().id(1).name("name1").owner(owner).requestId(1)
                .available(true).description("description1").build();

        List<Item> findItemslist = new ArrayList<>();
        findItemslist.add(item);

        Mockito.when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        Mockito.when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(findItemslist);

        ItemRequestDto findRequest = requestService.findByRequestId(itemRequest.getId());

        Assertions.assertNotNull(findRequest);
        Assertions.assertEquals(findRequest.getRequestorId(), requestorId);
        Assertions.assertEquals(findRequest.getDescription(), itemRequest.getDescription());
    }
}
