package ru.yandex.practicum.filmorate.exception;

public class FilmrateValidationException extends RuntimeException {
    public FilmrateValidationException(String message) {
        super(message);
    }
}
