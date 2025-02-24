package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public BookingDto mapToDto(Booking booking) {
        String startDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getStart());

        String endDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getEnd());

        return new BookingDto(booking.getId(), startDate, endDate,
                booking.getItem().getId(), booking.getBooker().getId(), booking.getStatus());
    }

    public Booking mapToBooking(BookingDto bookingDto, User booker, Item item) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTimeStart = LocalDateTime.parse(bookingDto.getStart(), dateTimeFormatter);
        ZonedDateTime zonedDateTimeStart = localDateTimeStart.atZone(ZoneOffset.UTC);
        Instant startDate = zonedDateTimeStart.toInstant();

        LocalDateTime localDateTimeEnd = LocalDateTime.parse(bookingDto.getEnd(), dateTimeFormatter);
        ZonedDateTime zonedDateTimeEnd = localDateTimeEnd.atZone(ZoneOffset.UTC);
        Instant endDate = zonedDateTimeEnd.toInstant();

        return new Booking(bookingDto.getId(), startDate, endDate,
                item, booker, bookingDto.getStatus());
    }

    public BookingDtoResponse mapToDtoResponse(Booking booking) {
        String startDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getStart());

        String endDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getEnd());

        return new BookingDtoResponse(booking.getId(), startDate, endDate,
                booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    public List<BookingDtoResponse> mapToDtoResponse(List<Booking> bookings) {
        return bookings.stream().map(booking -> mapToDtoResponse(booking)).toList();
    }

    public BookingDtoWithDate mapToBookingDtoWithDate(Booking booking) {
        return new BookingDtoWithDate(booking.getStart(), booking.getEnd());
    }
}