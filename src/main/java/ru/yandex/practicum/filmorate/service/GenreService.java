package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.Genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public List<Optional<Genre>> allGenre() {
        return genreDao.allGenre();
    }

    public Optional<Genre> getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }
}
