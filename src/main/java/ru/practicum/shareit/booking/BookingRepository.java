package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking as b " +
            "join b.booker as bk " +
            "where bk.id = ?1 " +
            "order by b.start")
    List<Booking> findAllByBookerId(Integer bookerId);

    //найти последнюю и следующую бронь
    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1")
    List<Booking> findItemsBooking(Integer itemId);
}