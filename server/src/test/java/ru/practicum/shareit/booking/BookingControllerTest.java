package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void addBooking_shouldAddBooking() throws Exception {
        ItemDto item = ItemDto.builder().id(1).build();
        UserDto booker = UserDto.builder().id(1).build();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        LocalDateTime endTime = startTime.plusMinutes(10);

        BookingDto bookingForAdd = BookingDto.builder().itemId(1).start(startTime)
                .end(endTime).build();

        BookingDtoResponse booking = BookingDtoResponse.builder().id(1).start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(10)).item(item)
                .booker(booker).status(Status.WAITING).build();

        when(bookingService.addBooking(bookingForAdd, 1)).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingForAdd))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty());
    }


    @Test
    void findBookingById_shouldFindBookingById() throws Exception {
        ItemDto item = ItemDto.builder().id(1).build();
        UserDto booker = UserDto.builder().id(1).build();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        LocalDateTime endTime = startTime.plusMinutes(10);

        BookingDtoResponse booking = BookingDtoResponse.builder().id(1).start(startTime)
                .end(endTime).item(item)
                .booker(booker).status(Status.APPROVED).build();

        when(bookingService.findBookingById(1)).thenReturn(booking);


        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty());
    }

    @Test
    void updateBookingStatus_shouldUpdateBookingStatus() throws Exception {
        ItemDto item = ItemDto.builder().id(1).build();
        UserDto booker = UserDto.builder().id(1).build();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        LocalDateTime endTime = startTime.plusMinutes(10);

        BookingDtoResponse booking = BookingDtoResponse.builder().id(1).start(startTime)
                .end(endTime).item(item)
                .booker(booker).status(Status.APPROVED).build();

        when(bookingService.updateBookingStatus(1, 1, true)).thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty());
    }

    @Test
    void findUsersBookings_shouldFindUsersBookings() throws Exception {
        ItemDto item = ItemDto.builder().id(1).build();
        UserDto booker = UserDto.builder().id(1).build();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        LocalDateTime endTime = startTime.plusMinutes(10);

        BookingDtoResponse booking = BookingDtoResponse.builder().id(1).start(startTime)
                .end(endTime).item(item)
                .booker(booker).status(Status.APPROVED).build();

        List<BookingDtoResponse> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingService.findUsersBookings(1, State.ALL)).thenReturn(bookingList);

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.[0].start").isNotEmpty())
                .andExpect(jsonPath("$.[0].end").isNotEmpty());
    }

    @Test
    void findUsersItemsBookings_shouldFindUsersItemsBookings() throws Exception {
        ItemDto item = ItemDto.builder().id(1).build();
        UserDto booker = UserDto.builder().id(1).build();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 10, 12, 0); // Укажи дату вручную
        LocalDateTime endTime = startTime.plusMinutes(10);

        BookingDtoResponse booking = BookingDtoResponse.builder().id(1).start(startTime)
                .end(endTime).item(item)
                .booker(booker).status(Status.APPROVED).build();

        List<BookingDtoResponse> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingService.findUsersItemsBookings(1, State.ALL)).thenReturn(bookingList);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.[0].start").isNotEmpty())
                .andExpect(jsonPath("$.[0].end").isNotEmpty());
    }

}
