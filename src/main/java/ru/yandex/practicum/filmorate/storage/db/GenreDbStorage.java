package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenrуStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenrуStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Genre> getById(Integer id) {
        try {
            String sql = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("genreId", id);
            Genre genre = namedParameterJdbcTemplate.queryForObject(sql, params, new GenreMapper());
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT genreId, genreName FROM genres ORDER BY genreId",
                new GenreMapper());
        return list;
    }

}
