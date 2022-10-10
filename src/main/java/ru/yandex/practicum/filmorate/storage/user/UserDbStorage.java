package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> allUser() {
        String sql = "select * from USERS";
        //return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(user, rs), user.getId());


        return null;
    }

    @Override
    public Optional<User> addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (NAME_USER, LOGIN, EMAIL, BIRTHDAY) VALUES ( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"ID_USER"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Пользователь {} добавлен в БД с ID {}", user, user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        int id = user.getId();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM USERS WHERE ID_USER = ?", id);
        if (userRows.next()) {
            String sqlQuery = "UPDATE USERS SET NAME_USER = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE ID_USER = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getName()
                    , user.getLogin()
                    , user.getEmail()
                    , user.getBirthday()
                    , id);
            log.info("Обновление данных пользователя в БД {}", user);
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} в БД не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %d в БД не найден.", id));
        }
    }
}
