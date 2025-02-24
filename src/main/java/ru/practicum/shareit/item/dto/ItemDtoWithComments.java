package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDtoWithComments {
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

