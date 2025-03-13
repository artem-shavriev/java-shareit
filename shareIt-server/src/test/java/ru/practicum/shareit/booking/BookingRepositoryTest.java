package ru.practicum.shareit.booking;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;


    @Test
    void shouldFindUsersBookingsByIdAndWaitingState() {
        LocalDateTime startBooking = LocalDateTime.now().minusHours(1);
        LocalDateTime endBooking = LocalDateTime.now().plusHours(1);

        User booker = User.builder().name("booker").email("@booker.com").build();
        booker = userRepository.save(booker);
        Integer bookerId = booker.getId();

        User owner = User.builder().name("owner").email("@owner.com").build();
        owner = userRepository.save(owner);

        ItemRequest request = ItemRequest.builder().created(startBooking).description("description")
                .requestorId(bookerId).build();
        requestRepository.save(request);

        Item item = Item.builder().name("name1").owner(owner).
                available(true).description("description1").build();
        item = itemRepository.save(item);

        Booking booking = Booking.builder().start(startBooking)
                .status(Status.WAITING).end(endBooking).item(item).booker(booker).build();
        bookingRepository.save(booking);

        List<Booking> bookingList = bookingRepository.findUsersBookingsByIdAndWaitingState(bookerId);
        Assertions.assertNotNull(bookingList.get(0));
    }
}
