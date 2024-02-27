package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

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
        getById(user.getId());
        String sql = "UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday WHERE user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday",  Date.valueOf(user.getBirthday()));
        params.addValue("user_id", user.getId());
        namedParameterJdbcTemplate.update(sql, params);
        return user;

    }

    @Override
    public List<User> getAll() {
        return namedParameterJdbcTemplate.query("SELECT * FROM users ", new UserMapper());
    }

    @Override
    public Optional<User> getById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id=:user_id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", id);
            User returnedUser = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
            return Optional.of(returnedUser);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public void putNewFriend(Long id, Long userId, boolean isFriend) {
        String sql = "INSERT INTO friends (user_id, friend_id, is_friend) VALUES(:user_id, :friend_id, :is_friend)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", userId);
        params.addValue("is_friend", isFriend);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void removeFriend(Long id, Long userId) {
        String sql = "SELECT is_friend FROM friends WHERE user_id=:user_id AND friend_id=:friend_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", userId);
        Boolean isFriend = namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
        namedParameterJdbcTemplate.update("DELETE FROM friends WHERE user_id=:user_id AND friend_id=:friend_id",
                params);
        if (isFriend) {
            namedParameterJdbcTemplate.update("UPDATE friends SET is_friend=false WHERE user_id=:user_id " +
                            "AND friend_id=:friend_id",
                    params);
        }
    }

    @Override
    public List<User> getUserFriends(Long id) {
        String sql = "SELECT u.* FROM users AS u " +
                "LEFT JOIN friends AS f ON u.user_id=f.friend_id " +
                "WHERE f.user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        List<User> list = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        return list;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "SELECT u.* FROM users AS u " +
                "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                "WHERE f.user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        List<User> firstUserFriends = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        params.addValue("user_id", otherId);
        List<User> secondUserFriends = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        List<User> resultList = secondUserFriends.stream().filter(firstUserFriends::contains)
                .filter(secondUserFriends::contains)
                .collect(Collectors.toList());
        return resultList;
    }

    @Override
    public void getFriendship(Long userId, Long friendId) {
        try {
            String sql = "SELECT is_friend FROM friends WHERE user_id=:user_id AND friend_id=:friend_id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", userId);
            params.addValue("friend_id", friendId);
            Boolean isFriends = namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public boolean isFriendship(Long userId, Long friendId) {
        try {
            getFriendship(userId, friendId);
            return true;
        } catch (NotFoundException e) {
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