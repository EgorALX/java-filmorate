package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@SuperBuilder
@NoArgsConstructor
@RequiredArgsConstructor
public class Film {

    @NonNull
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    @NotNull
    private LocalDate releaseDate;

    @NonNull
    @Positive
    @Min(1)
    private int duration;

    @NotNull
    private Mpa mpa;

    @Builder.Default
    private List<Genre> genres = new ArrayList<>();
}