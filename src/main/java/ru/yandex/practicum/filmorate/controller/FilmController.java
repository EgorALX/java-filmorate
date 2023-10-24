package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmrateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        return super.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        return super.update(film);
    }

    @GetMapping
    public List<Film> getAll(@Valid @RequestBody Film film) {
        log.info("Getting all films {}", film);
        return super.getAll();
    }

    @Override
    public void validate(Film data) {
        LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
        if (data.getReleaseDate().isBefore(startReleaseDate)) {
            throw new FilmrateValidationException("Film release date is invalid");
        }
    }
}
