package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaDao mpaDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, MpaDao mpaDao, LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Date is not valid");
        }
        Film newFilm = filmStorage.create(film);
        return newFilm;
    }

    public Film update(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film release date is invalid");
        }
        Film newFilm = filmStorage.update(film);
        return newFilm;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Optional<Film> getById(Long id) {
        Optional<Film> film = filmStorage.getById(id);
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
        likeDao.likeOnFilm(id, userId);
    }

    public void deleteLikeOnFilm(Long id, Long userId) {
        getById(id);
        getById(userId);
        likeDao.deleteLikeOnFilm(id, userId);
    }
}