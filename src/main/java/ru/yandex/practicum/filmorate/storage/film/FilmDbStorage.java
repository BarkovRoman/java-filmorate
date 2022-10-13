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
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sql = "SELECT* FROM FILMS WHERE ID_FILMS = ?";
        //String sql = "SELECT* FROM FILMS INNER JOIN MPA M on M.ID_MPA = FILMS.MPA WHERE ID_FILMS = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения Film по ID из базы данных");
        }
    }
    @Override
    public List<Optional<Film>> allFilm() {
        String sql = "SELECT* FROM FILMS";
        //String sql = "SELECT* FROM FILMS INNER JOIN MPA M on M.ID_MPA = FILMS.MPA";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Film из базы данных");
        }
    }

    private Optional<Film> makeFilm(ResultSet rs) {
        try {
            int id = rs.getInt("ID_FILMS");
            String name = rs.getString("NAME_FILMS");
            String description = rs.getString("DESCRIPTION");
            LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
            int duration = rs.getInt("DURATION");
            int idMpa = rs.getInt("MPA");
            //String nameMpa = rs.getString("NAME_MPA");

            Film film = new Film(id, name, description, releaseDate, duration, new Mpa(idMpa));

            log.info("Найден фильм в БД: {} ", film);
            likeOfFilm(film);
            //log.info("Like заполненны: {} ", film.getLike());
            return Optional.of(film);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Film из базы данных");
        }
    }

    private void likeOfFilm(Film film) {
        String sql = "SELECT USER_ID FROM 'LIKE' WHERE FILM_ID = ?";
        try {

            jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), film.getId()).forEach(film::addLike);


        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Like из базы данных");
        }
    }

    private Integer makeLike(ResultSet rs) {
        try {
            return rs.getInt("USER_ID");
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения ID USER Like из базы данных");
        }
    }

    @Override
    public Optional<Film> addLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        Optional<Film> film = getFilmById(filmId);

        film.ifPresent(film1 -> film1.addLike(userId));

        String sqlQuery = "INSERT INTO 'LIKE' (FILM_ID, USER_ID) VALUES(?, ?)";
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

        String sqlQuery = "DELETE FROM 'LIKE' WHERE FILM_ID = ? AND USER_ID = ?";
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
        String sql = "SELECT FILM_ID FROM 'LIKE' GROUP BY FILM_ID ORDER BY COUNT(USER_ID) DESC LIMIT ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), count).stream()
                    .map(this::getFilmById)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Film -> Like из БД");
        }
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME_FILMS, DESCRIPTION, RELEASE_DATE, DURATION, MPA) VALUES( ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"ID_FILMS"});
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
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        int id = film.getId();
        getFilmById(id)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Фильм с идентификатором %d в БД не найден.", id)));
        String sqlQuery = "UPDATE FILMS SET NAME_FILMS = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? WHERE ID_FILMS = ?";
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
        log.info("Обновление данных фильма в БД {}", film);
        return Optional.of(film);
    }
}
