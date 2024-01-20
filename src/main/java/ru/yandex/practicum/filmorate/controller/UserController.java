package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ValidateService validateService = new ValidateService();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User newUser = userService.create(user);
        log.info("Creating user {}", newUser);
        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        List<User> list = userService.getAll();
        log.info("Getting all users {}", list);
        return list;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Getting user");
        User user = userService.getById(id);
        if (user == null) {
            throw new NotFoundException("Data not found");
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Boolean putNewFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Putting friend {} to user {}", friendId, id);
        userService.putNewFriend(id, friendId);
        return true;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.deleteFriend(id, friendId);
        log.info("Putting like on film {}", user);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        log.info("Getting friends of user {}", id);
        List<User> friends = userService.getUserFriends(id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Getting common friends of user {} with user {}", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        return commonFriends;
    }
}