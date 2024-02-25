package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Genre {
    @NotNull
    @NonNull
    private Integer id;
    @NotNull
    private String name;
}