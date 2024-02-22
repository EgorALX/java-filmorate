package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.FilmMapper;

import java.sql.Date;
import java.util.*;

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
                film.getMpa().getMpa_id());
        Film createdFilm = jdbcTemplate.queryForObject(
                "SELECT film_id, name, description, release_date, duration, mpa_id FROM films WHERE name=? "
                        + "AND description=? AND release_date=? AND duration=? AND mpa_id=?",
                new FilmMapper(), film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getMpa_id());
        return createdFilm;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET name=?, description=?, release_date=?, " +
                "duration=?, mpa_id=? WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getMpa_id(),
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
                " GROUP BY l.film_id" +
                " ORDER BY COUND(l.user_id) DESC" +
                " LIMIT " + count, new FilmMapper());
        return sortedFilms;
    }

    @Override
    public void likeOnFilm(Long id, Long userId) {}

    @Override
    public void deleteLikeOnFilm(Long id, Long userId) {}

}