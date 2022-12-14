package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.Constants.COUNT_POPULAR_MOVIES;
import static ru.yandex.practicum.filmorate.Constants.RELEASE_DATA;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Optional<Film>> findAll() {
        return filmService.allFilm();
    }

    @PostMapping
    public Optional<Film> create(@Valid @RequestBody Film film) {
        validation(film);
        log.info("Создан фильм {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Optional<Film> put(@Valid @RequestBody Film film) {
        validation(film);
        log.info("Обновление фильма {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@PathVariable Integer id) {
        log.info("GET /films/{}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")  // Пользователь ставит лайк фильму
    public Optional<Film> addLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        log.info("Пользователь ID {} поставил лайк фильму ID {}",userId, filmId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")  // Пользователь удаляет лайк
    public Optional<Film> deleteLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        log.info("Пользователь ID {} удалил лайк фильму ID {}",userId, filmId);
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular") // Возвращает список из первых "count" фильмов по количеству лайков
    public List<Optional<Film>> findMoviesByLikes(
            @RequestParam(defaultValue = COUNT_POPULAR_MOVIES, required = false) @Positive Integer count) {
        log.info("Вывод списока из первых {} фильмов",count);
        return filmService.popularMovies(count);
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA) ) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}