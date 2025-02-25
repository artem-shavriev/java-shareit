package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
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
    @Transactional
    public BookingDtoResponse addBooking(BookingDto bookingDto, Integer userId) {

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет недоступен для брони.");
        }
        bookingDto.setStatus(Status.WAITING);

        Booking booking = bookingRepository.save(bookingMapper.mapToBooking(bookingDto, booker, item));
        log.info("Пользователь с id {}, создал бронь с id {} на вещь с id {}", userId, booking.getId(), item.getId());
        return bookingMapper.mapToDtoResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDtoResponse findBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        log.info("Найдена бронь по id {}", id);
        return bookingMapper.mapToDtoResponse(booking);
    }

    @Override
    @Transactional
    public BookingDtoResponse updateBookingStatus(Integer requestOwnerId, Integer bookingId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
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
        log.info("Обновлен статус бронирования с id {}", bookingId);
        return bookingMapper.mapToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findUsersBookings(Integer userId, State state) {
        List<Booking> allUsersBookings = bookingRepository.findAllByBookerIdOrderByStart(userId);

        log.info("Найдены все бронирования пользователя с id {}", userId);
        return bookingMapper.mapToDtoResponse(sortBookingByState(state, allUsersBookings));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findUsersItemsBookings(Integer userId, State state) {
        List<Item> findUserItems = itemRepository.findAllByOwnerId(userId);

        if (findUserItems.isEmpty() || findUserItems == null) {
            log.error("У пользователя c id {} нет вещей.", userId);
            throw new NotFoundException("У пользователя нет вещей.");
        }

        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> userItemsBookings = new ArrayList<>();

        allBookings.forEach(booking -> {
            if (findUserItems.contains(booking.getItem())) {
                userItemsBookings.add(booking);
            }
        });

        log.info("Найдены все бронирования вещей пользователя с id {}", userId);
        return bookingMapper.mapToDtoResponse(sortBookingByState(state, userItemsBookings));
    }

    public List<Booking> sortBookingByState(State state, List<Booking> allUsersBookings) {
        List<Booking> usersBookingsWithState = new ArrayList<>();

        if (state.equals(State.WAITING)) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.WAITING)) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals(State.CURRENT)) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.APPROVED)
                        && (booking.getStart().isBefore(LocalDateTime.now()) || booking.getStart().equals(Instant.now()))
                        && (booking.getEnd().isAfter(LocalDateTime.now()) || booking.getEnd().equals(Instant.now()))) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals(State.FUTURE)) {
            allUsersBookings.forEach(booking -> {
                if (booking.getStatus().equals(Status.APPROVED)
                        && (booking.getStart().isAfter(LocalDateTime.now()))) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals(State.PAST)) {
            allUsersBookings.forEach(booking -> {
                if ((booking.getStatus().equals(Status.APPROVED) || booking.getStatus().equals(Status.CANCELED))
                        && booking.getEnd().isBefore(LocalDateTime.now())) {
                    usersBookingsWithState.add(booking);
                }
            });

            return usersBookingsWithState;

        } else if (state.equals(State.REJECTED)) {
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
