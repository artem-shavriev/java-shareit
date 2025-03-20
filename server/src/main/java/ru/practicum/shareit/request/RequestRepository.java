package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Integer>  {
   List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Integer requestorId);

   List<ItemRequest> findAllByRequestorIdNotOrderByCreatedDesc(Integer requestorId);
}
