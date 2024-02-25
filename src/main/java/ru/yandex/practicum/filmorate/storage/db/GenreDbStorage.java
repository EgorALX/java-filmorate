package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public Genre getById(Integer id) {
        System.out.println(id);
        return jdbcTemplate.queryForObject("SELECT id, name" +
                        " FROM genres" +
                        " WHERE id=?",
                new GenreMapper(), id);
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = jdbcTemplate.query("SELECT id, name FROM genres ORDER BY id",
                new GenreMapper());
        return list;
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
