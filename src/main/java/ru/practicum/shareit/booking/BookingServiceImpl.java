package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(BookingDto bookingDto) {
        bookingDto.setStatus(Status.WAITING);

        User booker = userRepository.getById(bookingDto.getBookerId());
        Item item = itemRepository.getById(bookingDto.getItemId());

        Booking booking = bookingRepository.save(bookingMapper.mapToBooking(bookingDto, booker, item));

        return bookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto findBookingById(Integer id) {
        Booking booking = bookingRepository.getById(id);

        return bookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto updateBookingStatus(Integer requestOwnerId, Integer bookingId, Status status) {
        Booking booking = bookingRepository.getById(bookingId);
        Integer ownerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(requestOwnerId, ownerId)) {
            log.error("id алвдельца вещи {} не совпадает с id {} запроса.", ownerId, requestOwnerId);
            throw new ValidationException("Изменять статус брони может только владелец вещи, " +
                    "id владельца вещи не совпадает с id запрашивающего изменение пользователя");
        }

        booking.setStatus(status);
        return bookingMapper.mapToDto(bookingRepository.save(booking));
    }

}
