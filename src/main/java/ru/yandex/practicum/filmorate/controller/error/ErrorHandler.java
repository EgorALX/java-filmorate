package ru.yandex.practicum.filmorate.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Arrays;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundEcxeption(final NotFoundException exception) {
        log.info("Данные не найдены {}", exception.getMessage());
        return Map.of("Данные не найдены ", exception.getMessage(), "StackTrace: ",
                Arrays.toString(exception.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException exception) {
        log.info("Ошибка валидации {}", exception.getMessage());
        return Map.of("Ошибка валидации ", exception.getMessage(), "StackTrace: ",
                Arrays.toString(exception.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleServerException(final MethodArgumentNotValidException exception) {
        log.info("Ошибка валидации {}", exception.getMessage());
        return Map.of("Ошибка валидации ", exception.getMessage(), "StackTrace: ",
                Arrays.toString(exception.getStackTrace()));
    }
}
