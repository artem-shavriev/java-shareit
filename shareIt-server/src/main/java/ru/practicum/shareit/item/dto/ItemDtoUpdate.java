package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemDtoUpdate {
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;

    public boolean hasOwnerId() {
        return ownerId != null;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasAvailable() {
        return available != null;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }
}