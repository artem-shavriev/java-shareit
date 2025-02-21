package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Integer id;
    @NotNull
    private String start;
    @NotNull
    private String end;
    @NotNull
    private Integer itemId;
    private Integer bookerId;
    private Status status;
}
