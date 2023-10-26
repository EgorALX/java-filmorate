package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidateService {
    public void validateFilm(Film film) {
        LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
        if ((film.getId() == null) || film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Film release date is invalid");
        }
    }

    public void validateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("id = null");
        }
    }
}
