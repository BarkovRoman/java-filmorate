package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validation(film);
        id++;
        film.setId(id);
        log.debug("Добавлен фильм {}, всего фильмов {}", film, films.size());
        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        validation(film);
        int id = film.getId();

        if (films.containsKey(id)) {
            log.debug("Обновление данных фильма id {}, новые данные {}", film.getId(), film);
            films.put(id, film);
            return film;
        } else {
            log.debug("фильм с id {} не найден", film.getId());
            throw new ValidationException("Не верный ID");
        }
    }

    private void validation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка ввода названия");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Ошибка ввода описания, введено {} символов", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.error("Ошибка ввода даты релиза. Вы ввели {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error("Ошибка ввода продолжительности");
            throw new ValidationException("Продолжительность не может быть пустой");
        }
    }
}