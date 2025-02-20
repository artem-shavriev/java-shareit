package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public BookingDto updateBookingStatus(Integer requestOwnerId, Integer bookingId, boolean status) {
        Booking booking = bookingRepository.getById(bookingId);
        Integer ownerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(requestOwnerId, ownerId)) {
            log.error("id алвдельца вещи {} не совпадает с id {} запроса.", ownerId, requestOwnerId);
            throw new ValidationException("Изменять статус брони может только владелец вещи, " +
                    "id владельца вещи не совпадает с id запрашивающего изменение пользователя");
        }

        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingMapper.mapToDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> findUsersBookings(Integer userId, String state) {
        List<Booking> allUsersBookings = bookingRepository.findAllByBookerId(userId);

        return bookingMapper.mapToDto(sortBookingByState(state, allUsersBookings));
    }

    @Override
    public List<BookingDto> findUsersItemsBookings(Integer userId, String state) {
        List<Item> findUserItems = itemRepository.findAllByUserId(userId);

        if (findUserItems.isEmpty() || findUserItems == null) {
            log.error("У пользователя c id {} нет вещей.", userId);
            throw new ValidationException("У пользователя нет вещей.");
        }

        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> userItemsBookings = new ArrayList<>();

        allBookings.forEach(booking -> {
            if (findUserItems.contains(booking.getItem())) {
                userItemsBookings.add(booking);
            }
        });

        return bookingMapper.mapToDto(sortBookingByState(state, userItemsBookings));
    }


    public List<Booking> sortBookingByState(String state, List<Booking> allUsersBookings) {
        List<Booking> usersBookingsWithState = new ArrayList<>();

        if (state.equals("WAITING")) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.WAITING)) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals("CURRENT")) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.APPROVED)
                        && (booking.getStart().isBefore(Instant.now()) || booking.getStart().equals(Instant.now()))
                        && (booking.getEnd().isAfter(Instant.now()) || booking.getEnd().equals(Instant.now()))) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals("FUTURE")) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.APPROVED)
                        && (booking.getStart().isAfter(Instant.now()))) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals("PAST")) {
            allUsersBookings.forEach(booking -> {
                if ((booking.getStatus().equals(Status.APPROVED) || booking.getStatus().equals(Status.CANCELED))
                        && booking.getEnd().isBefore(Instant.now())) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals("REJECTED")) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.REJECTED)) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else {
            return allUsersBookings;
        }
    }
}
