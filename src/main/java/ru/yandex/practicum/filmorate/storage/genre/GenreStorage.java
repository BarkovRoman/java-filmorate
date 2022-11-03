package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
@Component
public interface GenreStorage {
    List<Optional<Genre>> allGenre();

    Optional<Genre> getGenreById(Integer id);
}
