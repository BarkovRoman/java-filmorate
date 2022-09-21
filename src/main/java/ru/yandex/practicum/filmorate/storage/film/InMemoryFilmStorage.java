package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;
    private final static LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    @Override
    public Collection<Film> allFilm() {
        return films.values();
    }
    @Override
    public Film addFilm(Film film) {
        validation(film);
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }
    @Override
    public Film updateFilm(Film film) {
        validation(film);
        int id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id ));
        }
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA) ) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
