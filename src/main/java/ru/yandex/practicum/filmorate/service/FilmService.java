package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryUserStorage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryUserStorage userStorage;
    private FilmStorage filmStorage = new InMemoryFilmStorage();

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id);
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count > filmStorage.getAll().size()) {
            count = filmStorage.getAll().size();
        }
        List<Film> sortedFilms = filmStorage.getAll().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
        return sortedFilms;
    }

    public Film likeOnFilm(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        film.getLikes().add(userId);
        return film;
    }

    public void deleteLikeOnFilm(Long id, Long userId) {
        System.out.println(filmStorage.getAll());
        System.out.println(userStorage.getAll());
        if (filmStorage.getById(id) == null || (userStorage.getById(userId)) == null) {
            throw new NotFoundException("Film not found");
        }
        filmStorage.getById(id).getLikes().remove(userId);
    }
}
