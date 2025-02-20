package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public BookingDto mapToDto(Booking booking) {
        String startDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getStart());

        String endDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getEnd());

        return new BookingDto(booking.getId(), startDate, endDate,
                booking.getItem().getId(), booking.getBooker().getId(), booking.getStatus());
    }

    public List<BookingDto> mapToDto(Iterable <Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();

        for (Booking b : bookings) {
            bookingDtos.add(mapToDto(b));
        }

        return bookingDtos;
    }

    public Booking mapToBooking(BookingDto bookingDto, User booker, Item item) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");
        LocalDateTime localDateTimeStart = LocalDateTime.parse(bookingDto.getStart(), dateTimeFormatter);
        ZonedDateTime zonedDateTimeStart = localDateTimeStart.atZone(ZoneOffset.UTC);
        Instant startDate = zonedDateTimeStart.toInstant();

        LocalDateTime localDateTimeEnd = LocalDateTime.parse(bookingDto.getEnd(), dateTimeFormatter);
        ZonedDateTime zonedDateTimeEnd = localDateTimeEnd.atZone(ZoneOffset.UTC);
        Instant endDate = zonedDateTimeEnd.toInstant();

        return new Booking(bookingDto.getId(), startDate, endDate,
                item, booker, bookingDto.getStatus());
    }
}