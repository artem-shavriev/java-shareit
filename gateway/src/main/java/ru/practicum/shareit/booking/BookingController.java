package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> findUsersBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("findUsersBookings with state {}, userId={}", stateParam, userId);
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findUsersItemsBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("findUsersItemsBookings with state {}, userId={}", stateParam, userId);
        return bookingClient.findUsersItemsBookings(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.addBooking(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@PathVariable Integer bookingId) {
        log.info("Get booking {}", bookingId);
        return bookingClient.findBookingById(bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }
}
