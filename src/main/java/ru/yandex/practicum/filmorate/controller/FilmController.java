package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.*;

import static ru.yandex.practicum.filmorate.Constants.COUNT_POPULAR_MOVIES;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.allFilm();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
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

    @GetMapping("/popular?count={count}") // Возвращает список из первых "count" фильмов по количеству лайков
    public Collection<Film> findMoviesByLikes(
            @RequestParam(defaultValue = COUNT_POPULAR_MOVIES, required = false) Integer count) {
        if (count < 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.popularMovies(count);
    }




}