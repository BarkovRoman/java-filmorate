package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Optional<Genre>> allGenre() {
        return genreStorage.allGenre();
    }

    public Optional<Genre> getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }
}
