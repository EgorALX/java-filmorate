//package ru.yandex.practicum.filmorate.storage.db;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.Mpa;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.db.dao.MpaDao;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@JdbcTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class FilmDbStorageTest {
//
//    @Autowired
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    private final MpaDao mpaDao;
//
//    @Test
//    public void create() {
//        FilmDbStorage filmDbStorage = new FilmDbStorage(namedParameterJdbcTemplate, new MpaDbStorage(new NamedParameterJdbcTemplate(new JdbcTemplate())));
//
//        Film film = new Film(1L, "name", "description", LocalDate.of(1990, 1, 1), 100);
//        film = filmDbStorage.create(film);
//
//        Optional<Film> savedFilm = filmDbStorage.getById(1L);
//
//        assertThat(savedFilm)
//                .isNotNull()
//                .usingRecursiveComparison()
//                .isEqualTo(Optional.of(film));
//    }
//
//    @Test
//    public void getByIdInvalid() {
//        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
//        assertThrows(NotFoundException.class, () -> {
//            userStorage.getById(9999L);
//        });
//    }
//
//}
