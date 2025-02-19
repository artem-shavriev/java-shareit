package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer item;
    private Integer booker;
    private Status status;
}
