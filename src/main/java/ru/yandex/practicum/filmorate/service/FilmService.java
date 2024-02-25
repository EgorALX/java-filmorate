package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmDbStorage filmStorage,
                         @Qualifier("UserDbStorage") UserDbStorage userStorage,
                         GenreDao genreDao,
                         MpaDao mpaDao,
                         LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Date is not valid");
        }
        Film newFilm = filmStorage.create(film);
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        filmStorage.addGenres(newFilm.getId(), film.getGenres());
        newFilm.setMpa(mpaDao.getById(film.getMpa().getId()));
        newFilm.setGenres(filmStorage.getGenres(newFilm.getId()));
        return newFilm;
    }

    public Film update(Film film) {
        if (!filmStorage.containsInBD(film.getId())) {
            throw new NotFoundException("Film not found");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film release date is invalid");
        }
        Film newFilm = filmStorage.update(film);
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        filmStorage.updateGenres(newFilm.getId(), film.getGenres());
        newFilm.setMpa(mpaDao.getById(film.getMpa().getId()));
        newFilm.setGenres(filmStorage.getGenres(newFilm.getId()));
        System.out.println(newFilm.getGenres());
        return newFilm;
    }

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        for (Film film : films) {
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
            film.setGenres(filmStorage.getGenres(film.getId()));
        }
        return films;
    }

    public Film getById(Long id) {
        if (!filmStorage.containsInBD(id)) {
            throw new NotFoundException("Film not found");
        }
        Film film = filmStorage.getById(id);
        film.setMpa(mpaDao.getById(film.getMpa().getId()));
        film.setGenres(filmStorage.getGenres(id));
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count < 1) {
            throw new ValidationException("count is invalid");
        }
        List<Film> films = filmStorage.getPopular(count);
        for (Film film : films) {
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
            film.setGenres(filmStorage.getGenres(film.getId()));
        }
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
        if (!filmStorage.containsInBD(id)) {
            throw new NotFoundException("Film not found");
        }
        if (!userStorage.containsInBD(userId)) {
            throw new NotFoundException("User not found");
        }
        likeDao.deleteLikeOnFilm(id, userId);
    }
}