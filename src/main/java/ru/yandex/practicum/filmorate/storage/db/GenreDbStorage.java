package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getById(Integer id) {
        try {
            String sql = "SELECT id, name FROM genres WHERE id=:id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            Genre genre = namedParameterJdbcTemplate.queryForObject(sql, params, new GenreMapper());
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = jdbcTemplate.query("SELECT id, name FROM genres ORDER BY id",
                new GenreMapper());
        return list;
    }

}
