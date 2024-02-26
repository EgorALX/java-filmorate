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

    @NonNull
    private Integer id;

    private String name;
}