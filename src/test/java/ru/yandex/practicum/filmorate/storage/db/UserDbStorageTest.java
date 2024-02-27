//package ru.yandex.practicum.filmorate.storage.db;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class UserDbStorageTest {
//
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    @Test
//    public void create() {
//        User newUser = new User(1L, "user@email.ru", "vanya123",
//                "Ivan Petrov", LocalDate.of(1990, 1, 1));
//        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
//        userStorage.create(newUser);
//
//        Optional<User> savedUser = userStorage.getById(1L);
//
//        assertThat(savedUser)
//                .isNotNull()
//                .usingRecursiveComparison()
//                .isEqualTo(Optional.of(newUser));
//    }
//
//    @Test
//    public void getByIdInvalid() {
//        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
//        assertThrows(EmptyResultDataAccessException.class, () -> {
//            userStorage.getById(9999L);
//        });
//    }
//}