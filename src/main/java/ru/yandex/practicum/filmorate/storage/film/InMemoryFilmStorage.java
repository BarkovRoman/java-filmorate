package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public List <Optional<Film>> allFilm() {
        return films.values().stream().map(Optional::of).collect(Collectors.toList());
    }
    @Override
    public Optional<Film> addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        return Optional.of(film);
    }
    @Override
    public Optional<Film> updateFilm(Film film) {
        int id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            return Optional.of(film);
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id ));
        }
    }
}
