package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.FriendshipDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private final UserStorage userStorage;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserDbStorage userStorage,
                         FriendshipDao friendshipDao) {
        this.userStorage = userStorage;
        this.friendshipDao = friendshipDao;
    }

    public User create(User user) {
        if (user == null) {
            throw new NotFoundException("User = null");
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null || userStorage.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }
        user.nameChange();
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public Boolean putNewFriend(Long id, Long userId) {
        if ((userStorage.getById(userId) == null) || (userStorage.getById(id) == null)) {
            throw new NotFoundException("User not found");
        }
        boolean isUsersFriends = friendshipDao.isFriendship(id, userId);
        friendshipDao.addFriend(id, userId, isUsersFriends);
        return true;
    }

    public void deleteFriend(Long id, Long userId) {
        if ((userStorage.getById(id) == null) || (userStorage.getById(userId) == null)) {
            throw new NotFoundException("User not found");
        }
        friendshipDao.removeFriend(id, userId);
    }

    public List<User> getUserFriends(Long id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("User not found");
        }
        List<User> friends = friendshipDao.getUserFriends(id);
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return friendshipDao.getCommonFriends(id, otherId);
    }
}