package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void addItem_shouldAddItemAnd() throws Exception {
        ItemDto itemToCreate = ItemDto.builder().ownerId(1).name("Item").description("description").available(true)
                .requestId(1).build();
        ItemDto itemCreated = ItemDto.builder().id(1).ownerId(1).name("Item").description("description").available(true)
                .requestId(1).build();

        when(itemService.addItem(itemToCreate, 1)).thenReturn(itemCreated);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemToCreate))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemCreated.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(itemCreated.getName()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.description").value(itemCreated.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(itemCreated.getAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId").value(itemCreated.getRequestId()));
    }

    @Test
    void updateItem_shouldUpdateItemAnd() throws Exception {
        ItemDtoUpdate itemToUpdate = ItemDtoUpdate.builder().ownerId(1).name("Item").description("description").available(true)
                .requestId(1).build();
        ItemDto updatedItem = ItemDto.builder().id(1).ownerId(1).name("Item").description("description").available(true)
                .requestId(1).build();

        when(itemService.updateItem(itemToUpdate, 1, 1)).thenReturn(updatedItem);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemToUpdate))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedItem.getName()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.description").value(updatedItem.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(updatedItem.getAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId").value(updatedItem.getRequestId()));
    }

    @Test
    void getItemById_shouldGetItemByIdAnd() throws Exception {
        BookingDtoWithDate lastBooking = BookingDtoWithDate.builder()
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusMinutes(30)).build();
        BookingDtoWithDate nextBooking = BookingDtoWithDate.builder()
                .start(LocalDateTime.now().plusMinutes(50)).end(LocalDateTime.now().plusMinutes(55)).build();

        CommentDto comment = CommentDto.builder()
                .id(0).text("text").itemName("name").authorName("author")
                .created(LocalDateTime.now().minusMinutes(30)).build();

        List<CommentDto> commentList = new ArrayList<>();
        commentList.add(comment);

        ItemDtoWithComments item = ItemDtoWithComments.builder().id(1).ownerId(1).name("Item").description("description")
                .available(true).requestId(1).lastBooking(lastBooking)
                .nextBooking(nextBooking).comments(commentList).build();

        when(itemService.getItem(anyInt())).thenReturn(item);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(item.getName()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.description").value(item.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(item.getAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId").value(item.getRequestId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastBooking").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextBooking").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].text")
                        .value(item.getComments().get(0).getText()));
    }

    @Test
    void getOwnerItems_shouldGetOwnerItemsAnd() throws Exception {
        BookingDtoWithDate lastBooking = BookingDtoWithDate.builder()
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusMinutes(30)).build();
        BookingDtoWithDate nextBooking = BookingDtoWithDate.builder()
                .start(LocalDateTime.now().plusMinutes(50)).end(LocalDateTime.now().plusMinutes(55)).build();

        CommentDto comment = CommentDto.builder()
                .id(0).text("text").itemName("name").authorName("author")
                .created(LocalDateTime.now().minusMinutes(30)).build();

        List<CommentDto> commentList = new ArrayList<>();
        commentList.add(comment);

        ItemWithBookingDateAndCommentsDto item = ItemWithBookingDateAndCommentsDto.builder().id(1).ownerId(1).name("Item").description("description")
                .available(true).requestId(1).lastBooking(lastBooking)
                .nextBooking(nextBooking).comments(commentList).build();

        List<ItemWithBookingDateAndCommentsDto> itemsList = new ArrayList<>();
        itemsList.add(item);

        when(itemService.getOwnerItems(1)).thenReturn(itemsList);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]name").value(item.getName()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]description").value(item.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]available").value(item.getAvailable()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]requestId").value(item.getRequestId()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]lastBooking").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]nextBooking").isNotEmpty())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]comments[0].text").value(item.getComments().get(0).getText()));
    }

    @Test
    void itemSearch_shouldSearchItemAnd() throws Exception {
        ItemDto item = ItemDto.builder().id(1).ownerId(1).name("Item").description("description").available(true)
                .requestId(1).build();

        List<ItemDto> itemList = new ArrayList<>();
        itemList.add(item);

        String searchText = "description";

        when(itemService.itemSearch(searchText)).thenReturn(itemList);

        mvc.perform(get("/items/search?text=description")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]name").value(item.getName()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]description").value(item.getDescription()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]available").value(item.getAvailable()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[0]requestId").value(item.getRequestId()));
    }

    @Test
    void addComment_shouldAddCommentAnd() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1).text("text").build();

        when(itemService.addComment(1, 1, comment)).thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.text").value(comment.getText()));
    }
}