package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    public Genre getById(Integer filmId);

    public List<Genre> getAll();

    boolean containsInBD(Integer id);
}
