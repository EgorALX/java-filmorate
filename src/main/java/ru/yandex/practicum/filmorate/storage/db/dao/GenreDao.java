package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    public void addGenres (Long filmId, Set<Genre> genres);
    public void updateGenres(Long filmId, Set<Genre> genres);
    public Set<Genre> getGenresOfFilm(Long filmId);
    public List<Genre> getAll();
}
