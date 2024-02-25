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
public class UserService {
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
        user.nameChange();
        return userStorage.create(user);
    }

    public User update(User user) {
        if (!userStorage.containsInBD(user.getId())) {
            throw new NotFoundException("User not found");
        }
        user.nameChange();
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        if (!userStorage.containsInBD(id)) {
            throw new NotFoundException("User not found");
        }
        User user = userStorage.getById(id);
        return user;
    }

    public Boolean putNewFriend(Long id, Long userId) {
        if ((!userStorage.containsInBD(id)) || (!userStorage.containsInBD(userId))) {
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