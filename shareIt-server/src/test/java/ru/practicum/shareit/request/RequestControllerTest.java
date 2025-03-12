package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {
    @MockBean
    private RequestService requestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void addItemRequest_shouldAddItemRequestAnd() throws Exception {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную

        ItemRequestDto itemRequestToCreate = ItemRequestDto.builder().description("description").created(create)
                .requestorId(1).build();

        ItemRequestDto createdItemRequest = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(1).build();

        when(requestService.addRequest(itemRequestToCreate, 1)).thenReturn(createdItemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestToCreate))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id").value(createdItemRequest.getId()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.description").value(createdItemRequest.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.created").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.requestorId").value(createdItemRequest.getRequestorId()));
    }

    @Test
    void findUserRequests_shouldFindUserRequestsAnd() throws Exception {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную

        ItemRequestDto request = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(1).build();

        List<ItemRequestDto> requestsList = new ArrayList<>();
        requestsList.add(request);

        when(requestService.findUserRequests(1)).thenReturn(requestsList);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].id").value(request.getId()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].description").value(request.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].created").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].requestorId").value(request.getRequestorId()));
    }

    @Test
    void findAllRequests_shouldFindAllRequestsAnd() throws Exception {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную

        ItemRequestDto request = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(1).build();

        List<ItemRequestDto> requestsList = new ArrayList<>();
        requestsList.add(request);

        when(requestService.findAllRequests(1)).thenReturn(requestsList);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].id").value(request.getId()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].description").value(request.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].created").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0].requestorId").value(request.getRequestorId()));
    }

    @Test
    void findByRequestsId_shouldFindByRequestsIdAnd() throws Exception {
        LocalDateTime create = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную

        ItemRequestDto request = ItemRequestDto.builder().id(1).description("description").created(create)
                .requestorId(1).build();

        when(requestService.findByRequestId(1)).thenReturn(request);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id").value(request.getId()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.description").value(request.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.created").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.requestorId").value(request.getRequestorId()));
    }
}
