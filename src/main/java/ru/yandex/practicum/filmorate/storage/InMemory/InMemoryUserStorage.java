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
    private final Map<Long, User> storage = new HashMap<>();
    private final Map<Long, HashSet<Long>> friends = new HashMap<Long, HashSet<Long>>();
    private long generateId;

    @Override
    public User create(User user) {
        user.setId(++generateId);
        user.nameChange();
        storage.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User update(User user) {
        storage.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
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
        friends.get(id).add(userId);
        friends.get(userId).add(id);
        return true;
    }

    @Override
    public void deleteFriend(Long id, Long userId) {
        friends.get(id).remove(userId);
        friends.get(userId).remove(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        List<User> friendsList = new ArrayList<>();
        for (Long userId : friends.get(id)) {
            friendsList.add(storage.get(userId));
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        if (friends.get(id).isEmpty() || friends.get(otherId).isEmpty()) {
            return new ArrayList<>();
        }
        List<User> commonFriends = new ArrayList<>();
        List<Long> commonFriendsId = friends.get(id).stream()
                .filter(userId -> friends.get(otherId).contains(userId))
                .collect(Collectors.toList());
        for (Long userId : commonFriendsId) {
            commonFriends.add(storage.get(userId));
        }
        return commonFriends;
    }
}