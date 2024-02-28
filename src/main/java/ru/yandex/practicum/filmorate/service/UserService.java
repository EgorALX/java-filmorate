package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.DbUserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(DbUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (user == null) {
            throw new NotFoundException("User = null");
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        userStorage.getById(user.getId()).orElseThrow(() -> new NotFoundException("Data not found"));
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        User user = userStorage.getById(id).orElseThrow(() -> new NotFoundException("Data not found"));
        return user;
    }

    public Boolean addFriend(Long id, Long userId) {
        // этими медодами проверяю существование пользователей
        getById(id);
        getById(userId);
        userStorage.addFriend(id, userId);
        return true;
    }

    public void deleteFriend(Long id, Long userId) {
        if ((userStorage.getById(id) == null) || (userStorage.getById(userId) == null)) {
            throw new NotFoundException("User not found");
        }
        userStorage.removeFriend(id, userId);
    }

    public List<User> getUserFriends(Long id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("User not found");
        }
        List<User> friends = userStorage.getFriends(id);
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}