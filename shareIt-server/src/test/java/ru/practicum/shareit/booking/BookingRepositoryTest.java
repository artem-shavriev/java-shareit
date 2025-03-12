package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    /*@Test
    void shouldFindUsersBookingsByIdAndWaitingState() {
        List<Booking> bookingList = bookingRepository.findUsersBookingsByIdAndWaitingState(1);
        Assertions.assertNotNull(bookingList);
    }*/
}
