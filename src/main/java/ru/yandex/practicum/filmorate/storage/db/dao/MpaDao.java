package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MpaDao {

    @NotNull Mpa getById(Integer id);

    List<Mpa> getAll();

}
