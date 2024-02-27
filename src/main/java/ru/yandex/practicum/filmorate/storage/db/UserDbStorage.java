package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.FriendshipMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (:email, :login, :name, :birthday)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday",  Date.valueOf(user.getBirthday()));
        namedParameterJdbcTemplate.update(sql, params);

        String sql1 = "SELECT user_id FROM users WHERE email=:email AND login=:login AND" +
                " name=:name AND birthday=:birthday";
        Long id = namedParameterJdbcTemplate.queryForObject(sql1, params, Long.class);
        user.setId(id);
        return user;
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
    public Optional<User> getById(Long id) {
        return Optional.of(jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE user_id=?",
                new UserMapper(), id));
    }

    @Override
    public boolean containsInBD(Long id) {
        try {
            Optional<User> user = getById(id);
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

    public static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    }
}
