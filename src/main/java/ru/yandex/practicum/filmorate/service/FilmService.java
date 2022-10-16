package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public List<Optional<Film>> allFilm() {
        return filmStorage.allFilm();
    }

    public Optional<Film> addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public Optional<Film> deleteLike(Integer filmId, Integer userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Optional<Film>> popularMovies(Integer count) { // вывод 10 наиболее популярных фильмов по количеству лайков
        return filmStorage.popularMovies(count);
    }

    public Optional<Film> getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }
}
