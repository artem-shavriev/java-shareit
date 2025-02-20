package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

@Service
public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto);

    BookingDto updateBookingStatus(Integer requestOwnerId, Integer bookingId, boolean status);

    BookingDto findBookingById(Integer id);

    List<BookingDto> findUsersBookings(Integer userId, String state);

    List<BookingDto> findUsersItemsBookings(Integer userId, String state);
}
