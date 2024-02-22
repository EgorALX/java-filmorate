package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.db.dao.LikeDao;


@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void likeOnFilm(Long filmId, Long userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)",
                filmId,
                userId);
    }

    @Override
    public void deleteLikeOnFilm(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?",
                filmId, userId);
    }
}