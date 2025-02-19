package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
interface UserRepository extends JpaRepository<User, Integer> {
    void deleteById(long id);
}
