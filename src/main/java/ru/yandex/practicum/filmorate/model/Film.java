package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@EqualsAndHashCode(of = {"id"})
@SuperBuilder
@NoArgsConstructor
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    @Min(1)
    private int duration;

    @NotNull
    private Mpa mpa;

    @NotNull
    @Builder.Default
    private LinkedHashSet<@NotNull Genre> genres = new LinkedHashSet<>();
}