package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    List<User> getAll();

    Optional<User> getById(Long id);

    void putNewFriend(Long id, Long userId, boolean isFriend);

    void removeFriend(Long id, Long userId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    Friendship getFriendship(Long userId, Long friendId);

    boolean isFriendship(Long userId, Long friendId);
}
