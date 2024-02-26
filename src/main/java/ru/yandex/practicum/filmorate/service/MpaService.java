package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.dao.MpaDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaDao;

    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }

    public Mpa getById(Integer id) {
        return mpaDao.getById(id);
    }
}
