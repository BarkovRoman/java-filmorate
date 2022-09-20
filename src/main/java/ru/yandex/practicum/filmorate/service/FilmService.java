package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Collections;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Film film, User user) {
        film.addLike(user.getId());
        return film;
    }

    public Film deleteLike(Film film, User user) {
        film.removeLike(user.getId());
        return film;
    }

    public Collection<Film> popularMovies(Film film) { // // вывод 10 наиболее популярных фильмов по количеству лайков
        return filmStorage.allFilm();
    }







}
