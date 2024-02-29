package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaStorage;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public @NotNull Optional<Mpa> getById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE mpaId=:mpaId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", id);

        List<Mpa> mpas = namedParameterJdbcTemplate.query(sql, params, new MpaMapper());
        return mpas.isEmpty() ? Optional.empty() : Optional.of(mpas.get(0));
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
