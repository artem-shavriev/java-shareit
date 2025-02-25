package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStart(Integer bookerId);

   @Query("select b " +
            "from Booking b "+
            "join b.booker as bb " +
            "where bb.id = ?1 and b.status = 'WAITING' " +
            "order by b.start")
    List<Booking> findBookingWithWaitingStatus(Integer bookerId);

    @Query("select b " +
            "from Booking b "+
            "join b.booker as bb " +
            "where bb.id = ?1 and b.status = 'APPROVED' " +
            "order by b.start")
    List<Booking> findBookingWithApprovedStatus(Integer bookerId);

    @Query("select b " +
            "from Booking b "+
            "join b.booker as bb " +
            "where bb.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start")
    List<Booking> findBookingWithRejectedStatus(Integer bookerId);

    List<Booking> findAllByItemId(Integer itemId);
}
