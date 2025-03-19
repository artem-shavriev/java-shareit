package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b " +
            "join b.booker br " +
            "where br.id = ?1 and b.status = 'WAITING' " +
            "order by b.start")
    List<Booking> findUsersBookingsByIdAndWaitingState(Integer bookerId);

    @Query("select b from Booking b " +
            "join b.booker br " +
            "where br.id = ?1 and (b.status = 'APPROVED' or b.status = 'WAITING') and b.start <= ?2 and b.end >= ?2 " +
            "order by b.start")
    List<Booking> findBookingByOwnerIdAndCurrentState(Integer bookerId, LocalDateTime now);

   @Query("select b from Booking b " +
            "join b.booker br " +
            "where br.id = ?1 and b.status = 'APPROVED' and b.start > ?2 " +
            "order by b.start")
    List<Booking> findBookingByOwnerIdAndFutureState(Integer bookerId, LocalDateTime now);

    @Query("select b from Booking b " +
            "join b.booker br " +
            "where br.id = ?1 and (b.status = 'APPROVED' or b.status = 'CANCELED') and b.end < ?2 " +
            "order by b.start")
    List<Booking> findBookingByOwnerIdAndPastState(Integer bookerId, LocalDateTime now);


    @Query("select b from Booking b " +
            "join b.booker br " +
            "where br.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start")
    List<Booking> findBookingByOwnerIdAndRejectedState(Integer bookerId, LocalDateTime now);

    @Query("select b from Booking b " +
            "join b.item i " +
            "where i.id = ?1 and b.status = 'WAITING' " +
            "order by b.start")
    List<Booking> findItemBookingsByIdAndWaitingState(Integer itemId);

    @Query("select b from Booking b " +
            "join b.item i " +
            "where i.id = ?1 and (b.status = 'APPROVED' or b.status = 'WAITING') and b.start <= ?2 and b.end >= ?2 " +
            "order by b.start")
    List<Booking> findItemBookingsByIdAndCurrentState(Integer itemId, LocalDateTime now);

    @Query("select b from Booking b " +
            "join b.item i " +
            "where i.id = ?1 and b.status = 'APPROVED' and b.start > ?2 " +
            "order by b.start")
    List<Booking> findItemBookingsByIdAndFutureState(Integer itemId, LocalDateTime now);

    @Query("select b from Booking b " +
            "join b.item i " +
            "where i.id = ?1 and (b.status = 'APPROVED' or b.status = 'CANCELED') and b.end < ?2 " +
            "order by b.start")
    List<Booking> findItemBookingsByIdAndPastState(Integer itemId, LocalDateTime now);


    @Query("select b from Booking b " +
            "join b.item i " +
            "where i.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start")
    List<Booking> findItemBookingsByIdAndRejectedState(Integer itemId, LocalDateTime now);

    List<Booking> findAllByItemIdOrderByStart(Integer itemId);

    List<Booking> findAllByBookerIdOrderByStart(Integer bookerId);

    List<Booking> findAllByItemId(Integer itemId);
}
