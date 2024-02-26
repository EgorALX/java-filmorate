package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.FriendshipMapper;
import ru.yandex.practicum.filmorate.storage.db.mappers.UserMapper;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) "
                        + "VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        User createdUser = jdbcTemplate.queryForObject(
                "SELECT user_id, email, login, name, birthday "
                        + "FROM users "
                        + "WHERE email=?", new UserMapper(), user.getEmail());
        return createdUser;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? "
                        + "WHERE user_id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        User updatedUser = jdbcTemplate.queryForObject(
                "SELECT user_id, email, login, name, birthday FROM users "
                        + "WHERE user_id=?", new UserMapper(), user.getId());
        return updatedUser;

    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ", new UserMapper());
    }

    @Override
    public User getById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE user_id=?",
                new UserMapper(), id);
    }

    @Override
    public boolean containsInBD(Long id) {
        try {
            User user = getById(id);
            log.trace("User {} found", user);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.trace("User with id {} not found", id);
            return false;
        }
    }

    @Override
    public void putNewFriend(Long id, Long userId, boolean isFriend) {
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
