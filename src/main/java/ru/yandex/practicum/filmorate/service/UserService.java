package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage = new InMemoryUserStorage();

    public User create(User user) {
        if (user == null) {
            throw new NotFoundException("User = null");
        }
        return storage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null || storage.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        user.nameChange();
        return storage.update(user);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getById(Long id) {
        User user = storage.getById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public Boolean putNewFriend(Long id, Long userId) {
        if ((storage.getById(userId) == null) || (storage.getById(id) == null)) {
            throw new NotFoundException("User not found");
        }
        storage.putNewFriend(id, userId);
        return true;
    }

    public void deleteFriend(Long id, Long userId) {
        if ((storage.getById(id) == null) || (storage.getById(userId) == null)) {
            throw new NotFoundException("User not found");
        }
        storage.deleteFriend(id, userId);
    }

    public List<User> getUserFriends(Long id) {
        if (storage.getById(id) == null) {
            throw new NotFoundException("User not found");
        }
        return storage.getUserFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        if (storage.getById(id).getFriends().isEmpty() || storage.getById(otherId).getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        return storage.getCommonFriends(id, otherId);
    }
}
