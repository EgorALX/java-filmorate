package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DbLikeStorageTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DbLikeStorage likeStorage;

    private DbUserStorage userStorage;

    private DbFilmStorage filmStorage;

    private Film newFilm;

    @BeforeEach
    protected void set() throws IOException {
        likeStorage = new DbLikeStorage(namedParameterJdbcTemplate);
        userStorage = new DbUserStorage(namedParameterJdbcTemplate);
        filmStorage = new DbFilmStorage(namedParameterJdbcTemplate);
        newFilm = new Film();
        assertNotNull(newFilm);
        newFilm.setName("filmNameGG");
        newFilm.setDescription("filAboutSomething");
        newFilm.setReleaseDate(LocalDate.of(1990, 1, 1));
        newFilm.setDuration(100);
        newFilm.setMpa(new Mpa(1, "G"));
        assertNotNull(newFilm);
    }

    @Test
    public void testLikeAndDeleteLike() {
        Film film = filmStorage.create(newFilm);
        assertNotNull(film);
        newFilm.setName("GGwdw");
        Film film2 = filmStorage.create(newFilm);
        assertNotNull(film);
        User newUser = new User();
        newUser.setEmail("user@email.ru");
        newUser.setLogin("vanya123");
        newUser.setName("Ivan Petrov");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));
        assertNotNull(newUser);
        User user = userStorage.create(newUser);
        likeStorage.likeOnFilm(film2.getId(), user.getId());
        List<Film> likedOnlikedFilms = filmStorage.getPopular(5);
        assertEquals(2, likedOnlikedFilms.size());
        assertEquals(2, likedOnlikedFilms.get(0).getId());
        likeStorage.deleteLikeOnFilm(film2.getId(), user.getId());
        assertNotNull(film);
        assertNotNull(film2);
    }
}
