package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaDao;

    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }

    public Mpa getById(Integer id) {
        return mpaDao.getById(id).orElseThrow(() -> new NotFoundException("Data not found"));
    }
}
