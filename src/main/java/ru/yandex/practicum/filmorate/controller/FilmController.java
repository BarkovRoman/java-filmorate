package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

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
    public List<Film> findAll() {
        return filmService.allFilm();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validation(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validation(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")  // Пользователь ставит лайк фильму
    public Film addLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")  // Пользователь удаляет лайк
    public Film deleteLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular") // Возвращает список из первых "count" фильмов по количеству лайков
    public List<Film> findMoviesByLikes(
            @RequestParam(defaultValue = COUNT_POPULAR_MOVIES, required = false) @Positive Integer count) {
        return filmService.popularMovies(count);
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA) ) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}