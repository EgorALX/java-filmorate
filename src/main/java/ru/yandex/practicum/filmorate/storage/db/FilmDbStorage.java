package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                        " VALUES (?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        Film createdFilm = jdbcTemplate.queryForObject(
                "SELECT film_id, name, description, release_date, duration, mpa_id FROM films WHERE name=? "
                        + "AND description=? AND release_date=? AND duration=? AND mpa_id=?",
                new FilmMapper(), film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        return createdFilm;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET" +
                        " name=?, description=?, release_date=?, duration=?, mpa_id=?" +
                        " WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        Film updatedFilm = getById(film.getId());
        return updatedFilm;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films", new FilmMapper());
        return films;
    }

    @Override
    public Film getById(Long id) {
        Film returnedFilm = jdbcTemplate.queryForObject("SELECT film_id, name, description, release_date, " +
                "duration, mpa_id FROM films WHERE film_id=?", new FilmMapper(), id);
        return returnedFilm;
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> sortedFilms = jdbcTemplate.query("SELECT * FROM films AS f " +
                "LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id" +
                " GROUP BY f.film_id" +
                " ORDER BY COUNT(l.user_id) DESC" +
                " LIMIT " + count, new FilmMapper());
        return sortedFilms;
    }

    @Override
    public void likeOnFilm(Long id, Long userId) {}

    @Override
    public void deleteLikeOnFilm(Long id, Long userId) {}

    @Override
    public List<Genre> getGenres(Long filmId) {
        List<Genre> genres = jdbcTemplate.query(
                "SELECT g.id, g.name FROM film_genres AS f " +
                        "LEFT OUTER JOIN genres AS g ON f.genre_id = g.id" +
                        " WHERE f.film_id=? ORDER BY g.id",
                new GenreMapper(), filmId);
        return genres;
    }

    @Override
    public void addGenres(Long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id)" +
                    " VALUES (?, ?)", filmId, genre.getId());
        }
    }

    @Override
    public void updateGenres(Long filmId, List<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }

    @Override
    public void deleteGenres(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id=?", filmId);
    }

    @Override
    public boolean containsInBD(Long id) {
        try {
            getById(id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }
}