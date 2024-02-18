package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Mpa {
    @NotNull
    @NonNull
    private Integer id;

    @NotNull
    private String name;
}
