package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

//    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
//    private static final LocalDate BIRTHDAY = LocalDate.now().minusDays(2);
//    private final FilmDbStorage filmDbStorage;
//    private final UserDbStorage userDbStorage;
//    private final MpaStorage mpaStorage;
//    private final GenreStorage genreStorage;
//    private final UserDbStorage userStorage;
//
//    @Test
//    public void testFilmById() {
//        Mpa mpa = mpaStorage.getMpaById(1).get();
//        Film film1 = new Film(0, "filmCreate",
//                RandomString.make(200), RELEASE_DATA, 100, mpa);
//
//        filmDbStorage.addFilm(film1);
//        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//
//    @Test
//    public void testAllFilm() {
//        Mpa mpa = mpaStorage.getMpaById(1).get();
//        Film film1 = new Film(0, "filmCreate",
//                RandomString.make(200), RELEASE_DATA, 100, mpa);
//        filmDbStorage.addFilm(film1);
//        List<Optional<Film>> filmOptional = filmDbStorage.allFilm();
//        assertThat(filmOptional).hasSize(1);
//
//        assertThat(filmOptional.get(0))
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//
//    @Test
//    public void testUpdateFilm() {
//        filmDbStorage.updateFilm(new Film(1, "newFilmCreate",
//                RandomString.make(200), RELEASE_DATA, 100, mpaStorage.getMpaById(1).get()));
//        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("name", "newFilmCreate")
//                );
//    }
//
//    @Test
//    public void testAddLikeFilm() {
//        userDbStorage.addUser(new User(1, "y.@yandex.ru", "dolore", "Nick Name", BIRTHDAY));
//        filmDbStorage.addLike(1,1);
//        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film.getLike()).contains(1, 1)
//                );
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film.getLike()).hasSize(1)
//                );
//    }
//
//    @Test
//    public void testPopularMovies() {
//        List<Optional<Film>> filmOptional = filmDbStorage.popularMovies(10);
//        assertThat(filmOptional).hasSize(1);
//        assertThat(filmOptional.get(0))
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//
//    @Test
//    public void testDeleteLikeFilm() {
//        filmDbStorage.deleteLike(1,1);
//        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film.getLike()).hasSize(0)
//                );
//    }
//
//    @Test
//    public void testGenreById() {
//        Optional<Genre> mpaOptional = genreStorage.getGenreById(1);
//        assertThat(mpaOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("name", "Комедия")
//                );
//    }
//
//    @Test
//    public void testAllGenre() {
//        List<Optional<Genre>> allGenre = List.of(Optional.of(new Genre(1, "Комедия")),
//                Optional.of(new Genre(2, "Драма")),
//                Optional.of(new Genre(3, "Мультфильм")),
//                Optional.of(new Genre(4, "Триллер")),
//                Optional.of(new Genre(5, "Документальный")),
//                Optional.of(new Genre(6, "Боевик")));
//
//        List<Optional<Genre>> genreOptional = genreStorage.allGenre();
//        assertThat(genreOptional).isEqualTo(allGenre);
//    }
//
//    @Test
//    public void testMpaById() {
//        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1);
//        assertThat(mpaOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("name", "G")
//                );
//    }
//
//    @Test
//    public void testAllMpa() {
//        List<Optional<Mpa>> allMpa = List.of(Optional.of(new Mpa(1, "G")),
//                Optional.of(new Mpa(2, "PG")),
//                Optional.of(new Mpa(3, "PG-13")),
//                Optional.of(new Mpa(4, "R")),
//                Optional.of(new Mpa(5, "NC-17")));
//
//        List<Optional<Mpa>> mpaOptional = mpaStorage.allMpa();
//        assertThat(mpaOptional).isEqualTo(allMpa);
//    }
//
//    @Test
//    public void testFindUserById() {
//        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", BIRTHDAY);
//        userStorage.addUser(user1);
//        Optional<User> userOptional = userStorage.getUserById(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//
//    @Test
//    public void testFindAllUser() {
//        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", BIRTHDAY);
//        userStorage.addUser(user1);
//        List<Optional<User>> userOptional = userStorage.allUser();
//        assertThat(userOptional.get(0))
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//
//    @Test
//    public void testUpdateUser() {
//        User user1 = new User(1, "yandex.@yandex.ru", "dolore", "Nick Name", BIRTHDAY);
//        userStorage.addUser(user1);
//        userStorage.updateUser(new User(1, "y.@yandex.ru", "dolore", "Nick Name", BIRTHDAY));
//        Optional<User> userOptional = userStorage.getUserById(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("email", "y.@yandex.ru")
//                );
//    }
//
//    @Test
//    public void testAddFriendsUser() {
//        userStorage.addFriends(1, 2);
//        Optional<User> userOptional = userStorage.getUserById(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user.getFriends()).contains(2, 2)
//                );
//    }
//
//    @Test
//    public void testSizeFriendsUser() {
//        Optional<User> userOptional = userStorage.getUserById(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user.getFriends()).hasSize(2)
//                );
//
//        Optional<User> userOptionalFriends = userStorage.getUserById(2);
//        assertThat(userOptionalFriends)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user.getFriends()).hasSize(1)
//                );
//    }
//
//    @Test
//    public void testDeleteFriendsUser() {
//        User user1 = new User(1, "y.@yandex.ru", "dolore1", "Nick Name1", BIRTHDAY);
//        userStorage.addUser(user1);
//
//        userStorage.deleteFriends(1, 2);
//        Optional<User> userOptional = userStorage.getUserById(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user.getFriends()).hasSize(0)
//                );
//    }
//
//    @Test
//    public void testMutualFriendsUser() {
//        userStorage.addFriends(1, 3);
//        userStorage.addFriends(2, 3);
//        userStorage.mutualFriends(1, 2);
//
//        List<Optional<User>> userOptional = userStorage.mutualFriends(1, 2);
//        assertThat(userOptional).hasSize(1);
//        assertThat(userOptional.get(0))
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 3)
//                );
//    }
//
//
//
//

}
