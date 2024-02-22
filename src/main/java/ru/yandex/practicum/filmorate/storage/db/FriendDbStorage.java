package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.db.mappers.FriendshipMapper;
import ru.yandex.practicum.filmorate.storage.db.mappers.UserMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long id, Long userId, boolean isFriend) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, is_friend)" +
                        " VALUES(?, ?, ?)", id, userId, isFriend);
    }

    @Override
    public void removeFriend(Long id, Long userId) {
        Friendship friendship = jdbcTemplate.queryForObject(
                "SELECT user_id, friend_id, is_friend FROM friends WHERE user_id=? AND friend_id=?",
                new FriendshipMapper(), id, userId);
        jdbcTemplate.update("DELETE FROM friends WHERE user_id=? AND friend_id=?", id, userId);
        if (friendship.getIsFriendship()) {
            jdbcTemplate.update("UPDATE friends SET is_friend=false WHERE user_id=? AND friend_id=?",
                    id, userId);
        }
    }

    @Override
    public List<User> getUserFriends(Long id) {
        return  jdbcTemplate.query("SELECT u.* FROM friends AS f" +
                                "LEFT OUTER JOIN users AS u ON f.user_id = u.user_id" +
                                " WHERE f.user_id=?",
                        new UserMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.query("SELECT * FROM users as u" +
                        "LEFT OUTER JOIN friends AS f ON f.user_id = u.user_id " +
                        "WHERE f.user_id=? AND f.friend_id=? AND f.is_friend=TRUE ", new UserMapper(),
                id, otherId);
    }

    @Override
    public Friendship getFriendship(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM friends WHERE user_id=? AND friend_id=?",
                new FriendshipMapper(), userId, friendId);
    }

    @Override
    public boolean isFriendship(Long userId, Long friendId) {
        try {
            getFriendship(userId, friendId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
