package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> storage = new HashMap<>();

    private long generateId;
    private ValidateService validateService = new ValidateService();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        user.setId(++generateId);
        validateService.validateUser(user);
        user.nameChange();
        storage.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        validateService.validateUser(user);
        if (!storage.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Data %s not found", user));
        }
        user.nameChange();
        storage.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        List<User> list = new ArrayList<>(storage.values());
        log.info("Getting all users {}", list);
        return list;
    }

}
