package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.MpaMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public Mpa getById(Integer id) {
        Mpa mpa = jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE mpa_id=?",
                new MpaMapper(), id);
        return mpa;
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = jdbcTemplate.query("SELECT * FROM mpa ORDER BY mpa_id",
                new MpaMapper());
        return allMpa;
    }
}
