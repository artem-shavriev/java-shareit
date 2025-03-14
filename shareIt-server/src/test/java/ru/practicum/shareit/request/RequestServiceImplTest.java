package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
public class RequestServiceImplTest {
    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestServiceImpl requestService;


}
