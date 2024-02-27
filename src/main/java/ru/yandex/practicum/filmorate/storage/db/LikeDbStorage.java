package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.db.dao.LikeDao;


@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void likeOnFilm(Long filmId, Long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (:film_id, :user_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteLikeOnFilm(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id=:film_id AND user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);
        namedParameterJdbcTemplate.update(sql, params);
    }
}