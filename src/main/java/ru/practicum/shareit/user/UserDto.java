package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    @Email
    private String email;

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }
}
