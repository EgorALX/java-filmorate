package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film getById(Long id);

    List<Film> getPopular(int count);

    void likeOnFilm(Long id, Long userId);

    void deleteLikeOnFilm(Long id, Long userId);

    void addGenres(Long filmId, List<Genre> genres);

    void updateGenres(Long filmId, List<Genre> genres);

    void deleteGenres(Long filmId);

    List<Genre> getGenres(Long filmId);
}