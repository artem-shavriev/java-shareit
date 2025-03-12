package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById() {
        User userForSave = User.builder().name("Add").email("@Add.com").build();

        User savedUser = userRepository.save(userForSave);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(savedUser.getName(), userForSave.getName());
    }
}
