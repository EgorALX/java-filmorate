package ru.yandex.practicum.filmorate.controller;

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
        log.info("Updating films {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Getting all films");
        List<Film> list = filmService.getAll();
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
        log.info("Getting popular {} films", count);
        List<Film> list = filmService.getPopular(count);
        return list;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeOnFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Putting like on film {} by user {}", id, userId);
        filmService.likeOnFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeOnFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Putting like by user {} on film {}", id, userId);
        filmService.deleteLikeOnFilm(id, userId);
    }
}


