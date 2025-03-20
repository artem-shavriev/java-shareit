package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemDto {
    private Integer id;
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}