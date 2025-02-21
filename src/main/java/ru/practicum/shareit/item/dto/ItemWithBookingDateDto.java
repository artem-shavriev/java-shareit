package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemWithBookingDateDto {
    private Integer id;
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private String start;
    private String end;
}
