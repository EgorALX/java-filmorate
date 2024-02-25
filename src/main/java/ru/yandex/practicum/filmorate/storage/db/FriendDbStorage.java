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
import java.util.stream.Collectors;

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
        List<User> list = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                "WHERE f.user_id=?",
                new UserMapper(), id);
        return list;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> firstUserFriends = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                        "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                        "WHERE f.user_id=? AND f.friend_id",
                new UserMapper(), id);
        List<User> secondUserFriends = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                        "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                        "WHERE f.user_id=?",
                new UserMapper(), otherId);
        List<User> resultList = secondUserFriends.stream().filter(firstUserFriends::contains)
                .filter(secondUserFriends::contains)
                .collect(Collectors.toList());
        return resultList;
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
