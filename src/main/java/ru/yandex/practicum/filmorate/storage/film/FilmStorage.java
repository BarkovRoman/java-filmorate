package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Component
public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    List<Film> allFilm();

}
