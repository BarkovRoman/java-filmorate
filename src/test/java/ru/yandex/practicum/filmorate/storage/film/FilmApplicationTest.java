package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmApplicationTest {

    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private static final LocalDate BIRTHDAY = LocalDate.now().minusDays(2);
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final MpaStorage mpaStorage;

    @Test
    public void testFilmById() {
        Mpa mpa = mpaStorage.getMpaById(1).get();
        Film film1 = new Film(0, "filmCreate",
                RandomString.make(200), RELEASE_DATA, 100, mpa);

        filmDbStorage.addFilm(film1);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testAllFilm() {
        Mpa mpa = mpaStorage.getMpaById(1).get();
        Film film1 = new Film(0, "filmCreate",
                RandomString.make(200), RELEASE_DATA, 100, mpa);
        filmDbStorage.addFilm(film1);
        List<Optional<Film>> filmOptional = filmDbStorage.allFilm();
        assertThat(filmOptional).hasSize(1);

        assertThat(filmOptional.get(0))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testUpdateFilm() {
        filmDbStorage.updateFilm(new Film(1, "newFilmCreate",
                RandomString.make(200), RELEASE_DATA, 100, mpaStorage.getMpaById(1).get()));
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "newFilmCreate")
                );
    }

    @Test
    public void testAddLikeFilm() {
        userDbStorage.addUser(new User(1, "y.@yandex.ru", "dolore", "Nick Name", BIRTHDAY));
        filmDbStorage.addLike(1,1);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLike()).contains(1, 1)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLike()).hasSize(1)
                );
    }

    @Test
    public void testPopularMovies() {
        List<Optional<Film>> filmOptional = filmDbStorage.popularMovies(10);
        assertThat(filmOptional).hasSize(1);
        assertThat(filmOptional.get(0))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

}
