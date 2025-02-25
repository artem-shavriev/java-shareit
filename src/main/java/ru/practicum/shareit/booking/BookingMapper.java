package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking mapToBooking(BookingDto bookingDto, User booker, Item item) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                item, booker, bookingDto.getStatus());
    }

    public BookingDtoResponse mapToDtoResponse(Booking booking) {
        return new BookingDtoResponse(booking.getId(),booking.getStart(), booking.getEnd(),
                itemMapper.mapToItemDto(booking.getItem()),
                userMapper.mapToUserDto(booking.getBooker()), booking.getStatus());
    }

    public List<BookingDtoResponse> mapToDtoResponse(List<Booking> bookings) {
        return bookings.stream().map(booking -> mapToDtoResponse(booking)).toList();
    }

    public BookingDtoWithDate mapToBookingDtoWithDate(Booking booking) {
        return new BookingDtoWithDate(booking.getStart(), booking.getEnd());
    }
}