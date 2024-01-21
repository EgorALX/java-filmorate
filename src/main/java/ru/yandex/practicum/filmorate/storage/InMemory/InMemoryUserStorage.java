package ru.yandex.practicum.filmorate.storage.InMemory;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Getter
@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final static Map<Long, User> storage = new HashMap<>();
    private long generateId;

    @Override
    public User create(User user) {
        user.setId(++generateId);
        if (user.getId() == null) {
            throw new ValidationException("id = null");
        }
        user.nameChange();
        user.setFriends(new HashSet<>());
        if (storage.containsValue(user)) {
            throw new DuplicateException();
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("id = null");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (!storage.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Data %s not found", user));
        }
        user.nameChange();
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
}