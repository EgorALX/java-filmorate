package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenrуStorage {

    Optional<Genre> getById(Integer filmId);

    List<Genre> getAll();
}
