package ru.yandex.practicum.filmorate.storage.InMemory;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final Map<Long, User> storage = new HashMap<>();
    private long generateId;

    @Override
    public User create(User user) {
        user.setId(++generateId);
        user.nameChange();
        user.setFriends(new HashSet<>());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User getById(Long id) {
        return storage.get(id);
    }

    @Override
    public Boolean putNewFriend(Long id, Long userId) {
        storage.get(id).getFriends().add(userId);
        storage.get(userId).getFriends().add(id);
        return true;
    }

    @Override
    public void deleteFriend(Long id, Long userId) {
        storage.get(id).getFriends().remove(userId);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        User user = storage.get(id);
        for (Long userId : user.getFriends()) {
            friends.add(storage.get(userId));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        List<Long> commonFriendsId = storage.get(id).getFriends().stream()
                .filter(userId -> storage.get(otherId).getFriends().contains(userId))
                .collect(Collectors.toList());
        for (Long userId : commonFriendsId) {
            commonFriends.add(storage.get(userId));
        }
        return commonFriends;
    }
}