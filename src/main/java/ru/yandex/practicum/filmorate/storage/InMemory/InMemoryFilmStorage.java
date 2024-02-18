package ru.yandex.practicum.filmorate.storage.InMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();
    private final Map<Long, HashSet<Long>> likes = new HashMap<>();

    private long generateId = 1;

    @Override
    public Film create(Film film) {
        film.setId(generateId++);
        storage.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        storage.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Film getById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Film> getPopular(int count) {
        int storageSize = storage.size();
        if (count > storageSize) {
            count = storageSize;
        }
        List<Film> sortedFilms = storage.values().stream()
                .sorted(Comparator.comparing(film -> likes.get(film.getId()).size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
        return sortedFilms;
    }

    @Override
    public void likeOnFilm(Long id, Long userId) {
        likes.get(id).add(userId);
    }

    @Override
    public void deleteLikeOnFilm(Long id, Long userId) {
        likes.get(id).remove(userId);
    }
}