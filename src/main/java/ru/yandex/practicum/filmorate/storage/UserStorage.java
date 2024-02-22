package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    List<User> getAll();

    User getById(Long id);

    void putNewFriend(Long id, Long userId);

    void deleteFriend(Long id, Long userId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
