package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.MpaMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(Integer id) {
        Mpa mpa = jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id=?",
                new MpaMapper(), id);
        return mpa;
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = jdbcTemplate.query("SELECT * FROM mpa ORDER BY id",
                new MpaMapper());
        return allMpa;
    }

    @Override
    public boolean containsInBD(Integer id) {
        try {
            getById(id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }
}
