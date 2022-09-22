package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        if (userId < 0) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
        film.addLike(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        if (!film.getLike().contains(userId)) {
            throw new UserNotFoundException(String.format("Пользователь № %d  Like не ставил", userId));
        }
        film.removeLike(userId);
        return film;
    }

    public Collection<Film> popularMovies(Integer count) { // вывод 10 наиболее популярных фильмов по количеству лайков
        return filmStorage.allFilm().stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        return filmStorage.allFilm().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Пользователь № %d не найден", id)));
    }


}
