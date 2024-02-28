package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DbFilmStorageTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DbFilmStorage filmStorage;

    private Film newFilm;

    @BeforeEach
    protected void set() {
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
    public void testCreateAndGetById() {
        Film film = filmStorage.create(newFilm);
        assertNotNull(film);
        assertThrows(NotFoundException.class, () -> {
            filmStorage.getById(9999L).orElseThrow(() -> new NotFoundException("Data not found"));
        });
        Film savedFilm = filmStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(savedFilm);
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testUpdate() {
        Film film = filmStorage.create(newFilm);
        assertNotNull(film);
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("film2");
        film1.setDescription("girrrr23");
        film1.setReleaseDate(LocalDate.of(1980, 1, 1));
        film1.setDuration(100);
        film1.setMpa(new Mpa(1, "G"));
        assertNotNull(film1);
        assertEquals(film, filmStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found")));
        filmStorage.update(film1);
        assertEquals(film1, filmStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found")));
        assertThrows(NotFoundException.class, () -> {
            filmStorage.getById(9999L).orElseThrow(() -> new NotFoundException("Data not found"));
        });
    }

    @Test
    public void testGetAll() {
        Film film = filmStorage.create(newFilm);
        assertNotNull(film);
        List<Film> filmsWithOneFilm = filmStorage.getAll();
        assertNotNull(filmsWithOneFilm);
        assertEquals(1, filmsWithOneFilm.size());
        assertEquals(film, filmsWithOneFilm.get(0));
        newFilm.setName("Chiby");
        Film film1 = filmStorage.create(newFilm);
        assertNotNull(film1);
        List<Film> filmsWithTwoFilm = filmStorage.getAll();
        assertNotNull(filmsWithTwoFilm);
        assertEquals(2, filmsWithTwoFilm.size());
    }

    @Test
    public void testCreaeteInvalide() {
        newFilm.setName(null);
        assertNotNull(newFilm);
        assertThrows(DataIntegrityViolationException.class, () -> {
            filmStorage.create(newFilm);
        });
    }

    @Test
    public void testGetPopular() {
        DbUserStorage userStorage = new DbUserStorage(namedParameterJdbcTemplate);
        DbLikeStorage likeStorage = new DbLikeStorage(namedParameterJdbcTemplate);
        Film film = filmStorage.create(newFilm);
        assertNotNull(film);
        newFilm.setName("chiby");
        Film chiby = filmStorage.create(newFilm);
        assertNotNull(chiby);
        newFilm.setName("ChibyPart2");
        Film chibyPart2 = filmStorage.create(newFilm);
        assertNotNull(chibyPart2);
        User firstUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        User secondUser = new User(2L, "secondUser@email.ru", "viktor111", "viktor",
                LocalDate.of(1980, 1, 1));
        assertNotNull(firstUser, "Null");
        assertNotNull(secondUser, "Null");
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        likeStorage.likeOnFilm(chibyPart2.getId(), firstUser.getId());
        likeStorage.likeOnFilm(chibyPart2.getId(), secondUser.getId());
        likeStorage.likeOnFilm(film.getId(), firstUser.getId());
        List<Film> popularFilms = filmStorage.getPopular(5);
        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.get(0).getId());
        assertEquals(1, popularFilms.get(1).getId());
        assertEquals(2, popularFilms.get(2).getId());
    }
}
