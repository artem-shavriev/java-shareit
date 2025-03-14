package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig
@SpringBootTest
public class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private BookingServiceImpl bookingService;

    @Test
    void shouldAddBooking() {
        LocalDateTime startBooking = LocalDateTime.now().minusHours(1);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(1);

        User booker = User.builder().id(1).name("booker").email("@booker.com").build();
        Integer bookerId = booker.getId();
        UserDto bookerDto = UserDto.builder().id(1).name("booker").email("@booker.com").build();

        User owner = User.builder().name("owner").email("@owner.com").build();

        ItemDto itemDto = ItemDto.builder().id(1).name("name1").ownerId(owner.getId()).
                available(true).description("description1").build();
        Item item = Item.builder().id(1).name("name1").owner(owner).
                available(true).description("description1").build();

        Item itemNotAvailable = Item.builder().id(2).name("name1").owner(owner).
                available(false).description("description1").build();

        Integer itemId = item.getId();

        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();

        BookingDto bookingDto = BookingDto.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).itemId(itemId).bookerId(bookerId).build();

        BookingDto bookingDtoWithUnAvailableItem = BookingDto.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).itemId(itemNotAvailable.getId()).bookerId(bookerId).build();

        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(booking);

        BookingDtoResponse savedBookingDto = bookingService.addBooking(bookingDto, bookerId);

        Assertions.assertNotNull(savedBookingDto);
        Assertions.assertEquals(savedBookingDto.getBooker().getName(), bookerDto.getName());
        Assertions.assertEquals(savedBookingDto.getItem().getName(), itemDto.getName());
        Assertions.assertEquals(savedBookingDto.getStatus(), Status.WAITING);

        Mockito.when(itemRepository.findById(itemNotAvailable.getId())).thenReturn(Optional.of(itemNotAvailable));

        assertThrows(ValidationException.class, () -> {
            bookingService.addBooking(bookingDtoWithUnAvailableItem, bookerId);
        });
    }

    @Test
    void shouldFindBookingById() {
        LocalDateTime startBooking = LocalDateTime.now().minusHours(1);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(1);

        User booker = User.builder().id(1).name("booker").email("@booker.com").build();

        UserDto bookerDto = UserDto.builder().id(1).name("booker").email("@booker.com").build();

        User owner = User.builder().name("owner").email("@owner.com").build();

        ItemDto itemDto = ItemDto.builder().id(1).name("name1").ownerId(owner.getId()).
                available(true).description("description1").build();
        Item item = Item.builder().id(1).name("name1").owner(owner).
                available(true).description("description1").build();

        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();

        Mockito.when(bookingRepository.findById(1)).thenReturn(Optional.ofNullable(booking));

        BookingDtoResponse savedBookingDto = bookingService.findBookingById(1);

        Assertions.assertNotNull(savedBookingDto);
        Assertions.assertEquals(savedBookingDto.getBooker().getName(), bookerDto.getName());
        Assertions.assertEquals(savedBookingDto.getItem().getName(), itemDto.getName());
        Assertions.assertEquals(savedBookingDto.getStatus(), Status.WAITING);
    }

    @Test
    void shouldUpdateBookingStatus() {
        LocalDateTime startBooking = LocalDateTime.now().minusHours(2);
        LocalDateTime endBooking = LocalDateTime.now().minusHours(1);

        User booker = User.builder().id(1).name("booker").email("@booker.com").build();
        Integer bookerId = booker.getId();
        UserDto bookerDto = UserDto.builder().id(1).name("booker").email("@booker.com").build();

        User owner = User.builder().id(1).name("owner").email("@owner.com").build();

        ItemDto itemDto = ItemDto.builder().id(1).name("name1").ownerId(owner.getId()).
                available(true).description("description1").build();
        Item item = Item.builder().id(1).name("name1").owner(owner).
                available(true).description("description1").build();

        Integer itemId = item.getId();

        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();

        Booking bookingWithRejectedStatus = Booking.builder().id(1).start(startBooking)
                .status(Status.REJECTED).end(endBooking).item(item).booker(booker).build();

        Booking bookingWithApprovedStatus = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(booker).build();

        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(bookingWithRejectedStatus);
        Mockito.when(bookingRepository.findById(1)).thenReturn(Optional.ofNullable(booking));

        BookingDtoResponse savedBookingDto = bookingService
                .updateBookingStatus(owner.getId(), booking.getId(), false);

        Assertions.assertNotNull(savedBookingDto);
        Assertions.assertEquals(savedBookingDto.getBooker().getName(), bookerDto.getName());
        Assertions.assertEquals(savedBookingDto.getItem().getName(), itemDto.getName());
        Assertions.assertEquals(savedBookingDto.getStatus(), Status.REJECTED);

        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(bookingWithApprovedStatus);
        BookingDtoResponse savedBookingDtoApproved = bookingService
                .updateBookingStatus(owner.getId(), booking.getId(), true);
        Assertions.assertNotNull(savedBookingDtoApproved);
        Assertions.assertEquals(savedBookingDtoApproved.getStatus(), Status.APPROVED);

        assertThrows(ValidationException.class, () -> {
            bookingService.updateBookingStatus(3, booking.getId(), false);
        });
    }

    @Test
    void shouldFindUsersBookings() {
        LocalDateTime startBooking = LocalDateTime.now().minusHours(2);
        LocalDateTime endBooking = LocalDateTime.now().minusHours(1);

        User booker = User.builder().id(1).name("booker").email("@booker.com").build();
        Integer bookerId = booker.getId();

        User owner = User.builder().name("owner").email("@owner.com").build();

        Item item = Item.builder().id(1).name("name1").owner(owner).
                available(true).description("description1").build();

        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListWaiting = new ArrayList<>();
        bookingListWaiting.add(booking);

        Booking bookingPast = Booking.builder().id(1).start(startBooking)
                .status(Status.CANCELED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListPast = new ArrayList<>();
        bookingListPast.add(bookingPast);

        Booking bookingCurrent = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListCurrent = new ArrayList<>();
        bookingListCurrent.add(bookingCurrent);

        Booking bookingFuture = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListFuture = new ArrayList<>();
        bookingListFuture.add(bookingFuture);

        Booking bookingRejected = Booking.builder().id(1).start(startBooking)
                .status(Status.REJECTED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListRejected = new ArrayList<>();
        bookingListRejected.add(bookingRejected);

        List<Booking> bookingListAll= new ArrayList<>();
        bookingListAll.add(bookingRejected);
        bookingListAll.add(bookingCurrent);
        bookingListAll.add(bookingPast);
        bookingListAll.add(bookingFuture);
        bookingListAll.add(booking);

        Mockito.when(bookingRepository
                .findUsersBookingsByIdAndWaitingState(bookerId)).thenReturn(bookingListWaiting);
        Mockito.when(bookingRepository
                .findBookingByOwnerIdAndPastState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListPast);
        Mockito.when(bookingRepository
                .findBookingByOwnerIdAndCurrentState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListCurrent);
        Mockito.when(bookingRepository
                        .findBookingByOwnerIdAndFutureState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListFuture);
        Mockito.when(bookingRepository
                        .findBookingByOwnerIdAndRejectedState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListRejected);
        Mockito.when(bookingRepository
                        .findBookingByOwnerIdAndAllState(ArgumentMatchers.any()))
                .thenReturn(bookingListAll);

        List<BookingDtoResponse> findBookingWaitingDtoList = bookingService.findUsersBookings(bookerId, State.WAITING);
        List<BookingDtoResponse> findBookingPastDtoList = bookingService.findUsersBookings(bookerId, State.PAST);
        List<BookingDtoResponse> findBookingCurrentDtoList = bookingService.findUsersBookings(bookerId, State.CURRENT);
        List<BookingDtoResponse> findBookingFutureDtoList = bookingService.findUsersBookings(bookerId, State.FUTURE);
        List<BookingDtoResponse> findBookingRejectedDtoList = bookingService.findUsersBookings(bookerId, State.REJECTED);
        List<BookingDtoResponse> findBookingAllDtoList = bookingService.findUsersBookings(bookerId, State.ALL);

        Assertions.assertEquals(findBookingWaitingDtoList.get(0).getStatus(), Status.WAITING);
        Assertions.assertEquals(findBookingPastDtoList.get(0).getStatus(), Status.CANCELED);
        Assertions.assertEquals(findBookingCurrentDtoList.get(0).getStatus(), Status.APPROVED);
        Assertions.assertEquals(findBookingFutureDtoList.get(0).getStatus(), Status.APPROVED);
        Assertions.assertEquals(findBookingRejectedDtoList.get(0).getStatus(), Status.REJECTED);
        Assertions.assertEquals(findBookingAllDtoList.size(), 5);
    }

    @Test
    void shouldFindUsersItemsBookings() { //мдописать
        LocalDateTime startBooking = LocalDateTime.now().minusHours(2);
        LocalDateTime endBooking = LocalDateTime.now().minusHours(1);

        User booker = User.builder().id(1).name("booker").email("@booker.com").build();

        User owner = User.builder().id(1).name("owner").email("@owner.com").build();
        Integer ownerId = owner.getId();

        Item item = Item.builder().id(1).name("name1").owner(owner).
                available(true).description("description1").build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Booking booking = Booking.builder().id(1).start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListWaiting = new ArrayList<>();
        bookingListWaiting.add(booking);

        Booking bookingPast = Booking.builder().id(1).start(startBooking)
                .status(Status.CANCELED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListPast = new ArrayList<>();
        bookingListPast.add(bookingPast);

        Booking bookingCurrent = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListCurrent = new ArrayList<>();
        bookingListCurrent.add(bookingCurrent);

        Booking bookingFuture = Booking.builder().id(1).start(startBooking)
                .status(Status.APPROVED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListFuture = new ArrayList<>();
        bookingListFuture.add(bookingFuture);

        Booking bookingRejected = Booking.builder().id(1).start(startBooking)
                .status(Status.REJECTED).end(endBooking).item(item).booker(booker).build();
        List<Booking> bookingListRejected = new ArrayList<>();
        bookingListRejected.add(bookingRejected);

        List<Booking> bookingListAll= new ArrayList<>();
        bookingListAll.add(bookingRejected);
        bookingListAll.add(bookingCurrent);
        bookingListAll.add(bookingPast);
        bookingListAll.add(bookingFuture);
        bookingListAll.add(booking);

        Mockito.when(itemRepository.findAllByOwnerId(ownerId)).thenReturn(itemList);
        Mockito.when(bookingRepository
                .findItemBookingsByIdAndWaitingState(item.getId())).thenReturn(bookingListWaiting);
        Mockito.when(bookingRepository
                        .findItemBookingsByIdAndPastState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListPast);
        Mockito.when(bookingRepository
                        .findItemBookingsByIdAndCurrentState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListCurrent);
        Mockito.when(bookingRepository
                        .findItemBookingsByIdAndFutureState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListFuture);
        Mockito.when(bookingRepository
                        .findItemBookingsByIdAndRejectedState(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(bookingListRejected);
        Mockito.when(bookingRepository
                        .findItemBookingsByIdAndAllState(ArgumentMatchers.any()))
                .thenReturn(bookingListAll);

        List<BookingDtoResponse> findBookingWaitingDtoList = bookingService
                .findUsersItemsBookings(ownerId, State.WAITING);
        List<BookingDtoResponse> findBookingPastDtoList = bookingService
                .findUsersItemsBookings(ownerId, State.PAST);
        List<BookingDtoResponse> findBookingCurrentDtoList = bookingService.
                findUsersItemsBookings(ownerId, State.CURRENT);
        List<BookingDtoResponse> findBookingFutureDtoList = bookingService.
                findUsersItemsBookings(ownerId, State.FUTURE);
        List<BookingDtoResponse> findBookingRejectedDtoList = bookingService.
                findUsersItemsBookings(ownerId, State.REJECTED);
        List<BookingDtoResponse> findBookingAllDtoList = bookingService.
                findUsersItemsBookings(ownerId, State.ALL);

        Assertions.assertEquals(findBookingWaitingDtoList.get(0).getStatus(), Status.WAITING);
        Assertions.assertEquals(findBookingPastDtoList.get(0).getStatus(), Status.CANCELED);
        Assertions.assertEquals(findBookingCurrentDtoList.get(0).getStatus(), Status.APPROVED);
        Assertions.assertEquals(findBookingFutureDtoList.get(0).getStatus(), Status.APPROVED);
        Assertions.assertEquals(findBookingRejectedDtoList.get(0).getStatus(), Status.REJECTED);
        Assertions.assertEquals(findBookingAllDtoList.size(), 5);

        List<Item> emptyList = new ArrayList<>();
        Mockito.when(itemRepository.findAllByOwnerId(ownerId)).thenReturn(emptyList);

        assertThrows(NotFoundException.class, () -> {
            bookingService.findUsersItemsBookings(ownerId, State.ALL);
        });

        assertThrows(NotFoundException.class, () -> {
            bookingService.
                    findUsersItemsBookings(ownerId, State.TEST);
        });
    }
}
