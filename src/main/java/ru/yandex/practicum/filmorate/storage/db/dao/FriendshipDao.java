package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.mappers.FriendshipMapper;

import java.util.List;

public interface FriendshipDao {

    void addFriend(Long id, Long userId, boolean isFriend);

    void removeFriend(Long id, Long userId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    public Friendship getFriendship(Long userId, Long friendId);

    public boolean isFriendship(Long userId, Long friendId);
}
