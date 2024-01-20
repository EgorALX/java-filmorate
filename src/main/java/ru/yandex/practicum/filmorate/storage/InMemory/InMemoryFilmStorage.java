package ru.yandex.practicum.filmorate.storage.InMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();

    private final ValidateService validateService = new ValidateService();
    private long generateId = 1;

    @Override
    public Film create(Film film) {
        validateService.validateFilm(film);
        film.setId(generateId++);
        film.setLikes(new HashSet<>());
        if (storage.containsValue(film)) {
            throw new ValidationException("Такой фильм уже существует");
        }
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        validateService.validateFilm(film);
        if (!storage.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Data %s not found", film));
        }
        film.setLikes(new HashSet<>());
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Film getById(long id) {
        return storage.get(id);
    }
}