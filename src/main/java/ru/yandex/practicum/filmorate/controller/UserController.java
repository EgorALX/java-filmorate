package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        return super.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        return super.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Getting all users {}", super.getAll());
        return super.getAll();
    }

    @Override
    public void validate(User data) {
        if (!data.getEmail().contains("@") || !data.getEmail().isBlank()) {
            throw new UserValidationException("User email is invalid");
        }
        if (data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
    }
}
