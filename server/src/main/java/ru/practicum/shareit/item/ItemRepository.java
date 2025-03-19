package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwnerId(Integer ownerId);

    @Query("select i from Item i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
            " or lower(i.description) like lower(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

    List<Item> findAllByRequestId(Integer requestId);

    List<Item> findByRequestIdIn(List<Integer> requestsId);
}
