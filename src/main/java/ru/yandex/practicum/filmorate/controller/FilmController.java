package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> storage = new HashMap<>();
    private ValidateService validateService = new ValidateService();

    private long generateId;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        film.setId(++generateId);
        validateService.validateFilm(film);
        storage.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        validateService.validateFilm(film);
        if (!storage.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Data %s not found", film));
        }
        storage.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> list = new ArrayList<>(storage.values());
        log.info("Getting all films {}", list);
        return list;
    }
}
