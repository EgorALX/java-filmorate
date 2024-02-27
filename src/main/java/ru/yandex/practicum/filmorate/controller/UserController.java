package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Getter
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user == null || user.getId() == null) {
            throw new ValidationException("User not found");
        }
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
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Boolean addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Putting friend {} to user {}", friendId, id);
        userService.addFriend(id, friendId);
        return true;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Deleting a friend {} from a user {}", friendId, id);
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