package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemWithBookingDateAndCommentsDto {
    private Integer id;
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private BookingDtoWithDate lastBooking;
    private BookingDtoWithDate nextBooking;
    private List<CommentDto> comments;
}
