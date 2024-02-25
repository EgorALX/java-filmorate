package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.dao.GenreDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Genre getGenre(Integer id) {
        if (!genreDao.containsInBD(id)) {
           throw new NotFoundException("Data not found");
        }
        return genreDao.getById(id);
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }
}
