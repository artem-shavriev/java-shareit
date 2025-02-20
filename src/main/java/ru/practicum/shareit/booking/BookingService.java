package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;

@Service
public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto);

    BookingDto updateBookingStatus(Integer requestOwnerId, Integer bookingId, Status status);

    BookingDto findBookingById(Integer id);



}
