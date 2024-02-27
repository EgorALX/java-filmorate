package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.validation.constraints.NotNull;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                " VALUES (:name, :description, :release_date, :duration, :mpa_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date",Date.valueOf(film.getReleaseDate()));
        params.addValue("duration",  film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        long id = keyHolder.getKey().longValue();
        film.setId(id);
        addGenres(id, film.getGenres());
        return film;
    }

    @Override
    public Film update(Film film) {
        getById(film.getId());
        String sql = "UPDATE films SET name=:name, description=:description, release_date=:release_date, " +
                "duration=:duration, mpa_id=:mpa_id WHERE film_id=:film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date",Date.valueOf(film.getReleaseDate()));
        params.addValue("duration",  film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        params.addValue("film_id", film.getId());
        namedParameterJdbcTemplate.update(sql, params);
        updateGenres(film.getId(), film.getGenres());
        film = getById(film.getId()).orElseThrow(() -> new NotFoundException("Data not found"));
        return film;
    }

    @Override
    public Optional<Film> getById(Long id) {
        try {

            String sql1 = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                    " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                    "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                    "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                    "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                    "WHERE f.film_id=:f.film_id " +
                    "ORDER BY f.film_id, g.genreId";

            String sql = "SELECT film_id, name, description, release_date, " +
                    "duration, mpa_id FROM films WHERE film_id=:film_id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("f.film_id", id);
            Film returnedFilm = namedParameterJdbcTemplate.queryForObject(sql1, params, new FilmMapper());
            return Optional.of(returnedFilm);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "GROUP BY f.film_id ORDER BY f.film_id";


        List<Film> films = namedParameterJdbcTemplate.query(sql, new OrderDetailsExtractor());

        return films;
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT :count ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);

        List<Film> films = namedParameterJdbcTemplate.query(sql, params, new OrderDetailsExtractor());
        return films;
    }


    private void addGenres(Long filmId, Set<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";

        List<SqlParameterSource> batch = new ArrayList<>();
        for (Genre genre : genres) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", filmId);
            params.addValue("genre_id", genre.getId());
            batch.add(params);
        }
        namedParameterJdbcTemplate.batchUpdate(sql, batch.toArray(new SqlParameterSource[0]));
    }

    private void updateGenres(Long filmId, Set<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }

    private void deleteGenres(Long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id=:film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(new Mpa(rs.getInt("mpaId"), rs.getString("mpaName")));
            do {
                if (rs.getInt("genreId" ) != 0) {
                    film.getGenres().add(new Genre(rs.getInt("genreId"), rs.getString("genreName")));
                }
            } while(rs.next());
            return film;
        }
    }

    private static class OrderDetailsExtractor implements ResultSetExtractor<List<Film>> {
        @Override
        public List<Film> extractData(ResultSet rs) throws SQLException {
            List<Film> orderDetailsList = new ArrayList<>();
            Film currentFilm = null;
            while (rs.next()) {
                Long filmId = rs.getLong("film_id");
                if (currentFilm == null || !filmId.equals(currentFilm.getId())) {
                    currentFilm = new Film();
                    currentFilm.setId(rs.getLong("film_id"));
                    currentFilm.setName(rs.getString("name"));
                    currentFilm.setDescription(rs.getString("description"));
                    currentFilm.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    currentFilm.setDuration(rs.getInt("duration"));
                    currentFilm.setMpa(new Mpa(rs.getInt("mpaId"), rs.getString("mpaName")));
                    orderDetailsList.add(currentFilm);
                }
                if (rs.getInt("genreId" ) != 0) {
                    currentFilm.getGenres().add(new Genre(rs.getInt("genreId"), rs.getString("genreName")));
                }
            }
            return orderDetailsList;
        }
    }
}