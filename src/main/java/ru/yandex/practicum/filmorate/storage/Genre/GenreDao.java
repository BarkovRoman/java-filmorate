package ru.yandex.practicum.filmorate.storage.Genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
@Component
public interface GenreDao {
    List<Optional<Genre>> allGenre();

    Optional<Genre> getGenreById(Integer id);
}
