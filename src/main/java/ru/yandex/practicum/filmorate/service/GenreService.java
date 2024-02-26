package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Optional<Genre> getById(Integer id) {
        return genreDao.getById(id);
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }
}
