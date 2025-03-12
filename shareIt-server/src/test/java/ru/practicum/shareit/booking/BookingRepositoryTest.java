package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
