package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor()
public class UserService {
    private final InMemoryUserStorage storage;

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public List<User> getAll() {
        List<User> list = storage.getAll();
        return list;
    }

    public User getById(Long id) {
        User user = storage.getById(id);
        return user;
    }

    public Boolean putNewFriend(Long id, Long userId) {
        if ((storage.getById(userId) == null) || (storage.getById(id) == null)) {
            throw new NotFoundException("User not found");
        }
        storage.getById(id).getFriends().add(userId);
        storage.getById(userId).getFriends().add(id);
        return true;
    }

    public User deleteFriend(Long id, Long userId) {
        if ((storage.getById(id) == null) || (storage.getById(userId) == null)) {
            throw new NotFoundException("User not found");
        }
        User user = storage.getById(id);
        user.getFriends().remove(userId);
        return user;
    }

    public List<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        User user = storage.getById(id);
        for (Long userId : user.getFriends()) {
            friends.add(storage.getById(userId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        if (storage.getById(id).getFriends().isEmpty() || storage.getById(otherId).getFriends().isEmpty()) {
            return commonFriends;
        }
        List<Long> commonFriendsId = storage.getById(id).getFriends().stream()
                .filter(userId -> storage.getById(otherId).getFriends().contains(userId))
                .collect(Collectors.toList());
        for (Long userId : commonFriendsId) {
            commonFriends.add(storage.getById(userId));
        }
        return commonFriends;
    }
}
