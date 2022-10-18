package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataBaseException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Optional<Genre>> allGenre() {
        String sql = "SELECT* FROM GENRES";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка GENRE из базы данных");
        }
    }

    public Optional<Genre> getGenreById(Integer id){
        String sql = "SELECT* FROM GENRES WHERE ID_GENRES = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения GENRE из базы данных");
        } catch (Throwable e) {
            throw new EntityNotFoundException(String.format("GENRE № %d в БД не найден!", id));
        }
    }

    private Optional<Genre> makeGenre(ResultSet rs) {
        try {
            int id = rs.getInt("ID_GENRES");
            String name = rs.getString("NAME_GENRES");
            Genre genre = new Genre(id, name);

            log.info("Найден жанр в БД: {} ", genre);
            return Optional.of(genre);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Mpa из базы данных");
        }
    }
}
