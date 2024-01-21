package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> list = filmService.getAll();
        log.info("Getting all films {}", list);
        return list;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Getting film");
        Film film = filmService.getById(id);
        if (film == null) {
            throw new NotFoundException("Data not found");
        }
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        List<Film> list = filmService.getPopular(count);
        log.info("Getting popular films {}", list);
        return list;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeOnFilm(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.likeOnFilm(id, userId);
        log.info("Putting like on film {}", film);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeOnFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLikeOnFilm(id, userId);
        log.info("Putting like by user {} on film {}", id, userId);
    }
}


