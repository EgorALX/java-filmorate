package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DbGenreStorageTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DbGenreStorage genreStorage;

    private DbFilmStorage filmStorage;

    @BeforeEach
    protected void set() throws IOException {
        genreStorage = new DbGenreStorage(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetById() {
        assertThrows(NotFoundException.class, () -> {
            genreStorage.getById(9999).orElseThrow(() -> new NotFoundException("Data not found"));
        });
        Genre genreId1 = genreStorage.getById(1).orElseThrow(() -> new NotFoundException("Data not found"));
        Genre genreId2 = genreStorage.getById(2).orElseThrow(() -> new NotFoundException("Data not found"));
        Genre genreId3 = genreStorage.getById(3).orElseThrow(() -> new NotFoundException("Data not found"));
        Genre genreId4 = genreStorage.getById(4).orElseThrow(() -> new NotFoundException("Data not found"));
        Genre genreId5 = genreStorage.getById(5).orElseThrow(() -> new NotFoundException("Data not found"));
        Genre genreId6 = genreStorage.getById(6).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(genreId1);
        assertNotNull(genreId2);
        assertNotNull(genreId3);
        assertNotNull(genreId4);
        assertNotNull(genreId5);
        assertNotNull(genreId6);
        assertEquals("Комедия", genreId1.getName());
        assertEquals("Драма", genreId2.getName());
        assertEquals("Мультфильм", genreId3.getName());
        assertEquals("Триллер", genreId4.getName());
        assertEquals("Документальный", genreId5.getName());
        assertEquals("Боевик", genreId6.getName());
    }

    @Test
    public void testGetAll() {
        List<Genre> genres = genreStorage.getAll();
        assertNotNull(genres);
        assertEquals(6, genres.size());
        Genre genreId1 = genres.get(0);
        Genre genreId2 = genres.get(1);
        Genre genreId3 = genres.get(2);
        Genre genreId4 = genres.get(3);
        Genre genreId5 = genres.get(4);
        Genre genreId6 = genres.get(5);
        assertNotNull(genreId1);
        assertNotNull(genreId2);
        assertNotNull(genreId3);
        assertNotNull(genreId4);
        assertNotNull(genreId5);
        assertNotNull(genreId6);
        assertEquals("Комедия", genreId1.getName());
        assertEquals("Драма", genreId2.getName());
        assertEquals("Мультфильм", genreId3.getName());
        assertEquals("Триллер", genreId4.getName());
        assertEquals("Документальный", genreId5.getName());
        assertEquals("Боевик", genreId6.getName());
    }

    @Test
    public void testFindByIds() {
        filmStorage = new DbFilmStorage(namedParameterJdbcTemplate);
        Film newFilm = new Film();
        assertNotNull(newFilm);
        newFilm.setName("filmNameGG");
        newFilm.setDescription("filAboutSomething");
        newFilm.setReleaseDate(LocalDate.of(1990, 1, 1));
        newFilm.setDuration(100);
        newFilm.setMpa(new Mpa(1, "G"));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(2));
        genres.add(new Genre(3));
        genres.add(new Genre(5));
        newFilm.setGenres(genres);
        filmStorage.create(newFilm);
        List<Genre> genres1 = genreStorage.findByIds(newFilm.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        assertEquals(genres1.size(), newFilm.getGenres().size());
    }
}
