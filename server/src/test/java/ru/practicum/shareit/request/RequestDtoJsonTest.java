package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoJsonTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void testUserDto() throws Exception {

        ItemDto itemDto = new ItemDto();
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1)
                .description("desc").requestorId(1)
                .created(LocalDateTime.of(2025, 03, 18, 12, 00, 00)).build();

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-03-18T12:00:00");
    }
}
