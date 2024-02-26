package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Optional<Film> getById(Long id);

    List<Film> getPopular(int count);

    void likeOnFilm(Long id, Long userId);

    void deleteLikeOnFilm(Long id, Long userId);

}