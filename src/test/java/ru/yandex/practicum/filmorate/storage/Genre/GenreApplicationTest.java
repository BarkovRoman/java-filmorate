package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreApplicationTest {
    private final GenreStorage genreStorage;

    @Test
    public void testGenreById() {
        Optional<Genre> mpaOptional = genreStorage.getGenreById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void testAllGenre() {
        List<Optional<Genre>> allGenre = List.of(Optional.of(new Genre(1, "Комедия")),
                Optional.of(new Genre(2, "Драма")),
                Optional.of(new Genre(3, "Мультфильм")),
                Optional.of(new Genre(4, "Триллер")),
                Optional.of(new Genre(5, "Документальный")),
                Optional.of(new Genre(6, "Боевик")));

        List<Optional<Genre>> genreOptional = genreStorage.allGenre();
        assertThat(genreOptional).isEqualTo(allGenre);
    }

}
