package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class User {
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    private HashSet<Long> friends;

    public void nameChange() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}
