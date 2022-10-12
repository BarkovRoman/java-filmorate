package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataBaseException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sql = "SELECT* FROM FILMS WHERE ID_FILMS = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения Film из базы данных");
        }
    }
    @Override
    public List<Optional<Film>> allFilm() {
        String sql = "SELECT* FROM FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Optional<Film> makeFilm(ResultSet rs) {
        try {
            int id = rs.getInt("ID_FILMS");
            String name = rs.getString("NAME_FILMS");
            String description = rs.getString("DESCRIPTION");
            LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
            int duration = rs.getInt("DURATION");

            Film film = new Film(id, name, description, releaseDate, duration, 2);
            likeOfFilm(film);
            log.info("Найден фильм: {} ", film);
            return Optional.of(film);
        } catch (DataAccessException | SQLException e) {
            throw new DataBaseException("Ошибка получения Film из базы данных");
        }
    }

    private void likeOfFilm(Film film) {
        String sql = "SELECT OTHER_ID FROM FRIENDS WHERE USER_ID = ?";
        jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), film.getId()).forEach(user::addFriends);
    }

    private Integer makeLike(ResultSet rs) {
        try {
            return rs.getInt("OTHER_ID");
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Friends из базы данных");
        }
    }

    @Override
    public Optional<Film> addLike(Integer filmId, Integer userId) {
        return Optional.empty();
    }

    @Override
    public Optional<Film> deleteLike(Integer filmId, Integer userId) {
        return Optional.empty();
    }

    @Override
    public List<Optional<Film>> popularMovies(Integer count) {
        return null;
    }










    @Override
    public Optional<Film> addFilm(Film film) {
        return null;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        return null;
    }

}
