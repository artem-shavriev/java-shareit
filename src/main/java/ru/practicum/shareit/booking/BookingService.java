package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

@Service
public interface BookingService {
    BookingDtoResponse addBooking(BookingDto bookingDto, Integer userId);

    BookingDtoResponse updateBookingStatus(Integer requestOwnerId, Integer bookingId, boolean status);

    BookingDtoResponse findBookingById(Integer id);

    List<BookingDtoResponse> findUsersBookings(Integer userId, State state);

    List<BookingDtoResponse> findUsersItemsBookings(Integer userId,  State state);
}
