package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenres(Long filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id)" +
                            " VALUES (?, ?)",
                    filmId,
                    genre.getGenre_id());
        }
    }

    @Override
    public void updateGenres(Long filmId, Set<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id=?", filmId);
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id)" +
                            " VALUES (?, ?)",
                    filmId,
                    genre.getGenre_id());
        }
    }

    @Override
    public Set<Genre> getGenresOfFilm(Long filmId) {
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(
                "SELECT * FROM film_genre AS f " +
                        "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id" +
                        "WHERE f.film_id=? ORDER BY f.genre_id",
                new GenreMapper(), filmId));
        return genres;
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY genre_id",
                new GenreMapper());
    }
}
