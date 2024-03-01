package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getById(Integer filmId);

    List<Genre> getAll();

    List<Genre> findByIds(List<Integer> ids);
}
