package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDtoUpdate {
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}