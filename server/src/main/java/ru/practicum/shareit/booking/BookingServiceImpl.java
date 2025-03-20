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
            log.error("id владельца вещи {} не совпадает с id {} запроса.", ownerId, requestOwnerId);
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

    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findUsersBookings(Integer userId, State state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case WAITING ->
                    bookingMapper.mapToDtoResponse(bookingRepository.findUsersBookingsByIdAndWaitingState(userId));
            case CURRENT ->
                    bookingMapper.mapToDtoResponse(bookingRepository.findBookingByOwnerIdAndCurrentState(userId, now));
            case FUTURE ->
                    bookingMapper.mapToDtoResponse(bookingRepository.findBookingByOwnerIdAndFutureState(userId, now));
            case PAST ->
                    bookingMapper.mapToDtoResponse(bookingRepository.findBookingByOwnerIdAndPastState(userId, now));
            case REJECTED ->
                    bookingMapper.mapToDtoResponse(bookingRepository.findBookingByOwnerIdAndRejectedState(userId, now));
            case ALL ->
                    //bookingMapper.mapToDtoResponse(bookingRepository.findBookingByOwnerIdAndAllState(userId));
                    bookingMapper.mapToDtoResponse(bookingRepository
                            .findAllByBookerIdOrderByStart(userId));
            default -> throw new NotFoundException("Неизвестный state");
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findUsersItemsBookings(Integer userId, State state) {
        List<Item> findUserItems = itemRepository.findAllByOwnerId(userId);

        if (findUserItems.isEmpty() || findUserItems == null) {
            log.error("У пользователя c id {} нет вещей.", userId);
            throw new NotFoundException("У пользователя нет вещей.");
        }

        List<BookingDtoResponse> userItemsBookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case WAITING:
                findUserItems.forEach(item -> {
                    bookingRepository.findItemBookingsByIdAndWaitingState(item.getId()).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            case CURRENT:
                findUserItems.forEach(item -> {
                    bookingRepository.findItemBookingsByIdAndCurrentState(item.getId(), now).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            case FUTURE:
                findUserItems.forEach(item -> {
                    bookingRepository.findItemBookingsByIdAndFutureState(item.getId(), now).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            case PAST:
                findUserItems.forEach(item -> {
                    bookingRepository.findItemBookingsByIdAndPastState(item.getId(), now).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            case REJECTED:
                findUserItems.forEach(item -> {
                    bookingRepository.findItemBookingsByIdAndRejectedState(item.getId(), now).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            case ALL:
                findUserItems.forEach(item -> {
                    bookingRepository.findAllByItemIdOrderByStart(item.getId()).forEach(booking ->
                            userItemsBookings.add(bookingMapper.mapToDtoResponse(booking)));
                });
                log.info("Найдены все бронирования вещей пользователя с id {} и state {}", userId, state);
                return userItemsBookings;

            default:
                throw new NotFoundException("Неизвестный state");
        }
    }
}
