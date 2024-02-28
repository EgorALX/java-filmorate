package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DbMpaStorageTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DbMpaStorage mpaStorage;

    @BeforeEach
    protected void set() throws IOException {
        mpaStorage = new DbMpaStorage(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetById() {
        assertThrows(NotFoundException.class, () -> {
            mpaStorage.getById(9999).orElseThrow(() -> new NotFoundException("Data not found"));
        });
        Mpa mpaId1 = mpaStorage.getById(1).orElseThrow(() -> new NotFoundException("Data not found"));
        Mpa mpaId2 = mpaStorage.getById(2).orElseThrow(() -> new NotFoundException("Data not found"));
        Mpa mpaId3 = mpaStorage.getById(3).orElseThrow(() -> new NotFoundException("Data not found"));
        Mpa mpaId4 = mpaStorage.getById(4).orElseThrow(() -> new NotFoundException("Data not found"));
        Mpa mpaId5 = mpaStorage.getById(5).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(mpaId1);
        assertNotNull(mpaId2);
        assertNotNull(mpaId3);
        assertNotNull(mpaId4);
        assertNotNull(mpaId5);
        assertEquals("G", mpaId1.getName());
        assertEquals("PG", mpaId2.getName());
        assertEquals("PG-13", mpaId3.getName());
        assertEquals("R", mpaId4.getName());
        assertEquals("NC-17", mpaId5.getName());
    }

    @Test
    public void testGetAll() {
        List<Mpa> mpas = mpaStorage.getAll();
        assertNotNull(mpas);
        assertEquals(5, mpas.size());
        Mpa mpaId1 = mpas.get(0);
        Mpa mpaId2 = mpas.get(1);
        Mpa mpaId3 = mpas.get(2);
        Mpa mpaId4 = mpas.get(3);
        Mpa mpaId5 = mpas.get(4);
        assertNotNull(mpaId1);
        assertNotNull(mpaId2);
        assertNotNull(mpaId3);
        assertNotNull(mpaId4);
        assertNotNull(mpaId5);
        assertEquals("G", mpaId1.getName());
        assertEquals("PG", mpaId2.getName());
        assertEquals("PG-13", mpaId3.getName());
        assertEquals("R", mpaId4.getName());
        assertEquals("NC-17", mpaId5.getName());
    }
}
