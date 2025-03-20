package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDateAndCommentsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoJsonTest {
    private final JacksonTester<ItemDto> itemJson;
    private final JacksonTester<CommentDto> commentJson;
    private final JacksonTester<ItemWithBookingDateAndCommentsDto> itemWithBookingDateAndCommentsDtoJson;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(
                1,
                1,
                "name",
                "description",
                true,
                1
        );

        JsonContent<ItemDto> result = itemJson.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "itemName",
                "authorName",
                LocalDateTime.of(2025, 03,18,12,00, 00));

        JsonContent<CommentDto> result = commentJson.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.itemName").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("authorName");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-03-18T12:00:00");
    }

    @Test
    void testItemDtoWithBookingDateAndComments() throws Exception {
        LocalDateTime start = LocalDateTime
                .of(2025, 03,18,12,00, 00);
        LocalDateTime end = LocalDateTime
                .of(2025, 03,18,13,00, 00);

        BookingDtoWithDate nextBooking = new BookingDtoWithDate(start.plusDays(10), end.plusDays(10));
        BookingDtoWithDate lastBooking = new BookingDtoWithDate(start, end);

        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "itemName",
                "authorName",
                LocalDateTime.of(2025, 03,18,12,00, 00));

        List<CommentDto> commentsList = new ArrayList<>();
        commentsList.add(commentDto);

        ItemWithBookingDateAndCommentsDto dto = new ItemWithBookingDateAndCommentsDto(
                1,
                1,
                "name",
                "description",
                true,
                1,
                lastBooking,
                nextBooking,
                commentsList
        );

        JsonContent<ItemWithBookingDateAndCommentsDto> result = itemWithBookingDateAndCommentsDtoJson.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

}
