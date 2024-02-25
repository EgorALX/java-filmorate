package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.UserMapper;

import java.sql.Date;
import java.util.*;

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
    public void putNewFriend(Long id, Long userId) {
    }

    @Override
    public void deleteFriend(Long id, Long userId) {
    }

    @Override
    public List<User> getUserFriends(Long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return null;
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
}
