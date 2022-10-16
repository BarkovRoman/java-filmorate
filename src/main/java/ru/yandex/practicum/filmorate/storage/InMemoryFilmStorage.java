package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final UserStorage userStorage;
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

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.ofNullable(films.values().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Фильм № %d не найден", id))));
    }

    @Override
    public Optional<Film> addLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        Optional<Film> film = getFilmById(filmId);
        film.ifPresent(film1 -> film1.addLike(userId));
        return film;
    }

    @Override
    public Optional<Film> deleteLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        Optional<Film> film = getFilmById(filmId);
        film.ifPresent(film1 -> film1.removeLike(userId));
        return film;
    }

    @Override
    public List<Optional<Film>> popularMovies(Integer count) {
        return films.values().stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                .map(Optional::of)
                .collect(Collectors.toList());
    }
}
