package ru.yandex.practicum.filmorate.storage.db.dao;

public interface LikeStorage {

    void likeOnFilm(Long id, Long userId);

    void deleteLikeOnFilm(Long id, Long userId);
}
