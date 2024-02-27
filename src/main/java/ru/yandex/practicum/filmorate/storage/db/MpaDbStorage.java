package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaStorage;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public @NotNull Optional<Mpa> getById(Integer id) {
        try {
            String sql = "SELECT * FROM mpa WHERE mpaId=:mpaId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("mpaId", id);
            Mpa mpa = namedParameterJdbcTemplate.queryForObject(sql, params, new MpaMapper());
            return Optional.of(mpa);
        }  catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = namedParameterJdbcTemplate.query("SELECT * FROM mpa ORDER BY mpaId",
                new MpaMapper());
        return allMpa;
    }

    public static class MpaMapper implements RowMapper<Mpa> {

        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpaId"));
            mpa.setName(rs.getString("mpaName"));
            return mpa;
        }
    }
}
