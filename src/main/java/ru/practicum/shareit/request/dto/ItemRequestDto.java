package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private Integer requestor;

    @JsonFormat
    private LocalDateTime created;
}
