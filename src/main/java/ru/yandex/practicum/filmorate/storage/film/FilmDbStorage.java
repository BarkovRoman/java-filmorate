package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataBaseException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor

public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sql = "SELECT* FROM FILMS f " +
                "LEFT JOIN MPA m on m.id_mpa = f.mpa WHERE id_films = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения Film по ID из базы данных");
        } catch (Throwable e) {
            throw new EntityNotFoundException(String.format("Film № %d в БД не найден!", id));
        }
    }

    @Override
    public List<Optional<Film>> allFilm() {
        String sql = "SELECT* FROM FILMS f " +
                "LEFT JOIN MPA m on m.id_mpa = f.mpa ";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Film из базы данных");
        }
    }

    private Optional<Film> makeFilm(ResultSet rs) {
        try {
            int id = rs.getInt("id_films");
            String name = rs.getString("name_films");
            String description = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            int duration = rs.getInt("duration");
            int idMpa = rs.getInt("mpa");
            String nameMpa = rs.getString("name_mpa");

            Film film = new Film(id, name, description, releaseDate, duration, new Mpa(idMpa, nameMpa));

            log.info("Найден фильм в БД: {} ", film);
            genreOfFilm(film);
            likeOfFilm(film);
            return Optional.of(film);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Film из базы данных");
        }
    }

    private void likeOfFilm(Film film) {
        String sql = "SELECT user_id FROM LIKES WHERE film_id = ?";
        try {
            jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), film.getId()).forEach(film::addLike);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Like из базы данных");
        }
    }

    private Integer makeLike(ResultSet rs) {
        try {
            return rs.getInt("user_id");
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения ID USER Like из базы данных");
        }
    }

    private void genreOfFilm(Film film) {
        String sql = "SELECT* FROM FILM_GENRES fg " +
                "LEFT JOIN GENRES g ON fg.genre_id  = g.id_genres " +
                "WHERE fg.film_id = ?";
        try {
            jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId()).forEach(film::addGenre);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения Film по ID из базы данных");
        }
    }

    private Genre makeGenre(ResultSet rs) {
        try {
            return new Genre(rs.getInt("id_genres"), rs.getString("name_genres"));
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения ID_GENRES из базы данных");
        }
    }

    @Override
    public Optional<Film> addLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        Optional<Film> film = getFilmById(filmId);

        film.ifPresent(film1 -> film1.addLike(userId));

        String sqlQuery = "INSERT INTO LIKES (film_id, user_id) VALUES(?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка добавления Like в БД");
        }
        log.info("Пользователь ID {} поставил Like фильму ID {}", userId, filmId);

        return film;
    }

    @Override
    public Optional<Film> deleteLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        Optional<Film> film = getFilmById(filmId);

        film.ifPresent(film1 -> film1.removeLike(userId));

        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка удаления Like из БД");
        }
        log.info("Пользователь ID {} удалил Like фильму ID {}", userId, filmId);

        return film;
    }

    @Override
    public List<Optional<Film>> popularMovies(Integer count) {
        String sql = "SELECT* FROM FILMS f " +
                "LEFT JOIN MPA m on m.id_mpa = f.mpa " +
                "LEFT JOIN LIKES l ON f.id_films = l.film_id " +
                "GROUP BY f.id_films ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Film -> Like из БД");
        }
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        String sqlQueryFilm = "INSERT INTO FILMS (name_films, description, release_date, duration, mpa) VALUES( ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlQueryFilm, new String[]{"id_films"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
            }, keyHolder);

        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка добавления Film в БД");
        }

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Фильм {} добавлен в БД", film);
        film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).get().getName());
        if (film.getGenres().size() != 0) {
            addGenre(film);
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        int id = film.getId();
        getFilmById(id);

        String sqlQuery = "UPDATE FILMS SET name_films = ?, description = ?, release_date = ?, duration = ?, mpa = ? WHERE id_films = ?";
        try {
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , id);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка обновления Film в БД");
        }
        addGenre(film);
        log.info("Обновление данных фильма в БД {}", film);
        film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).get().getName());

        return getFilmById(id);
    }

    private void addGenre(Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE film_id = ?";
        try {
            jdbcTemplate.update(sqlQuery, film.getId());
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка удаления Like из БД");
        }

        film.getGenres()
                .forEach(genre -> {
                    String sqlQueryGenre = "INSERT INTO FILM_GENRES(film_id, genre_id) VALUES (?, ?)";
                    try {
                        jdbcTemplate.update(sqlQueryGenre
                                , film.getId()
                                , genre.getId()
                        );
                    } catch (DataAccessException e) {
                        throw new DataBaseException("Ошибка обновления жанра Film в БД");
                    }
                });
        film.getGenres().clear();
        genreOfFilm(film);
        log.info("Обновление жанра фильма {}", film);
    }
}