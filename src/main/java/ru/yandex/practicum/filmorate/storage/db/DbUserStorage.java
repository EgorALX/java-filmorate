package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
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
public class DbUserStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User create(User user) {
        user.nameChange();
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (:email, :login, :name, :birthday)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday",  Date.valueOf(user.getBirthday()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        Long id = keyHolder.getKey().longValue();

        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
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
        String sql = "SELECT * FROM users WHERE user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        List<User> users = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));

    }

    @Override
    public void addFriend(Long id, Long userId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES(:user_id, :friend_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", userId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void removeFriend(Long id, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", userId);
        namedParameterJdbcTemplate.update("DELETE FROM friends WHERE user_id=:user_id AND friend_id=:friend_id",
                params);
    }

    @Override
    public List<User> getFriends(Long id) {
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