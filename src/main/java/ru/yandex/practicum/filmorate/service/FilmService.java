package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        /*Film film = getFilmById(filmId);
        if (userService.getUserById(userId).isEmpty() || userId < 0) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
        film.addLike(userId);
        return film;*/
    }

    public Optional<Film> deleteLike(Integer filmId, Integer userId) {
        return filmStorage.deleteLike(filmId, userId);
        /*Film film = getFilmById(filmId);

        if (userService.getUserById(userId).isEmpty() || userId < 0) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }

        if (!film.getLike().contains(userId)) {
            throw new UserNotFoundException(String.format("Пользователь № %d  Like Film № %d не ставил", userId, filmId));
        }

        film.removeLike(userId);
        return film;*/
    }

    public List<Optional<Film>> popularMovies(Integer count) { // вывод 10 наиболее популярных фильмов по количеству лайков
        return filmStorage.popularMovies(count);
        /*return filmStorage.allFilm().stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());*/
    }

    public Optional<Film> getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
        /*return filmStorage.allFilm().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Пользователь № %d не найден", id)));*/
    }
}
