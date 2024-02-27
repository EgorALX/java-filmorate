package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Genre getById(Integer id) {
        return genreDao.getById(id).orElseThrow(() -> new NotFoundException("Data not found"));
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }
}
