package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

public interface MpaDao {
    Mpa getById(Integer id);

    List<Mpa> getAll();

    boolean containsInBD(Integer id);
}
