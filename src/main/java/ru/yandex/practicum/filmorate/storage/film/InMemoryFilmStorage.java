package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public List<Film> allFilm() {
        return List.copyOf(films.values());
    }
    @Override
    public Film addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }
    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id ));
        }
    }
}
