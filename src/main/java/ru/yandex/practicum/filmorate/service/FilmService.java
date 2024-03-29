package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;


    @Autowired
    public FilmService(DbFilmStorage filmStorage, DbUserStorage userStorage, MpaStorage mpaStorage,
                       LikeStorage likeStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Film create(Film film) {
        mpaStorage.getById(film.getMpa().getId()).orElseThrow(() -> new NotFoundException("Data not found"));
        List<Genre> genres = genreStorage.findByIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Some genres not found");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getMpa() == null)) {
            throw new ValidationException("Date is not valid");
        }
        Film newFilm = filmStorage.create(film);
        return newFilm;
    }

    public Film update(Film film) {
        mpaStorage.getById(film.getMpa().getId()).orElseThrow(() -> new NotFoundException("Data not found"));
        List<Genre> genres = genreStorage.findByIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Some genres not found");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getMpa() == null)) {
            throw new ValidationException("Date is not valid");
        }
        filmStorage.getById(film.getId());
        Film newFilm = filmStorage.update(film);
        return newFilm;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id).orElseThrow(() -> new NotFoundException("Data not found"));
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count < 1) {
            throw new ValidationException("count is invalid");
        }
        List<Film> films = filmStorage.getPopular(count);
        return films;
    }

    public void likeOnFilm(Long id, Long userId) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Film not found");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        likeStorage.likeOnFilm(id, userId);
    }

    public void deleteLikeOnFilm(Long id, Long userId) {
        getById(id);
        getById(userId);
        likeStorage.deleteLikeOnFilm(id, userId);
    }
}