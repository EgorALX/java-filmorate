package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Genre> getById(Integer id) {
        String sql = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", id);
        List<Genre> genres = namedParameterJdbcTemplate.query(sql, params, new GenreMapper());
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT genreId, genreName FROM genres ORDER BY genreId",
                new GenreMapper());
        return list;
    }

    @Override
    public List<Genre> findByIds(List<Integer> ids) {
        String sql = "SELECT * FROM genres g WHERE g.genreId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, new GenreMapper());
    }
}
