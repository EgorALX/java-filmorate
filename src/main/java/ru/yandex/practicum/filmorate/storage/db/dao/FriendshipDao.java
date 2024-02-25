package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {

    void addFriend(Long id, Long userId, boolean isFriend);

    void removeFriend(Long id, Long userId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    public Friendship getFriendship(Long userId, Long friendId);

    public boolean isFriendship(Long userId, Long friendId);
}
