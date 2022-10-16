package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {
    List<Optional<Film>> allFilm();
    Optional<Film> addFilm(Film film);
    Optional<Film> updateFilm(Film film);
    Optional<Film> getFilmById(Integer id);
    Optional<Film> addLike(Integer filmId, Integer userId);
    Optional<Film> deleteLike(Integer filmId, Integer userId);
    List<Optional<Film>> popularMovies(Integer count);






}
