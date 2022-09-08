package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final static LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        validation(film);
        id++;
        film.setId(id);
        log.debug("Добавлен фильм {}, всего фильмов {}", film, films.size());
        films.put(id, film);
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> put(@Valid @RequestBody Film film) {
        validation(film);
        int id = film.getId();

        if (films.containsKey(id)) {
            log.debug("Обновление данных фильма id {}, новые данные {}", film.getId(), film);
            films.put(id, film);
            return ResponseEntity.status(HttpStatus.OK).body(film);
        } else {
            log.debug("фильм с id {} не найден", film.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    private void validation(Film film) {
       if (film.getReleaseDate().isBefore(RELEASE_DATA) ) {
            log.error("Ошибка ввода даты релиза. Вы ввели {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}