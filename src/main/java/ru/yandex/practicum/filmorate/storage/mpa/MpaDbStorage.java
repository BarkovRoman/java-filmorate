package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataBaseException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Optional<Mpa>> allMpa() {
        String sql = "SELECT* FROM MPA";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка Mpa из базы данных");
        }
    }

    public Optional<Mpa> getMpaById(Integer id){
        String sql = "SELECT* FROM MPA WHERE id_mpa = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения Mpa из базы данных");
        } catch (Throwable e) {
            throw new EntityNotFoundException(String.format("Mpa № %d в БД не найден!", id));
        }
    }

    private Optional<Mpa> makeMpa(ResultSet rs) {
        try {
            int id = rs.getInt("id_mpa");
            String name = rs.getString("name_mpa");
            Mpa mpa = new Mpa(id, name);

            log.info("Найден Mpa в БД: {} ", mpa);
            return Optional.of(mpa);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Mpa из базы данных");
        }
    }



}
