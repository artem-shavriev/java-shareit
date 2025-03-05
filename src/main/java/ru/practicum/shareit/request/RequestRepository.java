package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Integer>  {
    /*@Query("select i from Item i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
            " or lower(i.description) like lower(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);*/
}
