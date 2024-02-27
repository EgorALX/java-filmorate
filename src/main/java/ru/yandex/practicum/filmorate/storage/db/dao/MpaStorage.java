package ru.yandex.practicum.filmorate.storage.db.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    @NotNull Optional<Mpa> getById(Integer id);

    List<Mpa> getAll();

}
