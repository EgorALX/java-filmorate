package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.db.mappers.MpaMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaDao {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(Integer id) {
        try {
            String sql = "SELECT * FROM mpa WHERE id=:id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            Mpa mpa = namedParameterJdbcTemplate.queryForObject(sql, params, new MpaMapper());
            return mpa;
        }  catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = jdbcTemplate.query("SELECT * FROM mpa ORDER BY id",
                new MpaMapper());
        return allMpa;
    }
}
