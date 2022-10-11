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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Optional<User>> allUser() {
        String sql = "SELECT* FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private Optional<User> makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_USER");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME_USER");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        User user = new User(id, email, login, name, birthday);
        friendsOfUser(user);
        log.info("Найден пользователь: {} ", user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM USERS WHERE ID_USER = ?", id);
        if (userRows.next()) {
            User user = new User(
                    id,
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME_USER"),
                    Objects.requireNonNull(userRows.getDate("BIRTHDAY")).toLocalDate());
            friendsOfUser(user);

            log.info("Найден пользователь: {}}", user);
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    private void friendsOfUser(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT OTHER_ID FROM FRIENDS WHERE USER_ID = ?", user.getId());
        if (userRows.next()) {
            user.addFriends(userRows.getInt("OTHER_ID"));
        }
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
        if (getUserById(id).isEmpty()) {
            log.info("Пользователь с идентификатором {} в БД не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %d в БД не найден.", id));
        }
        String sqlQuery = "UPDATE USERS SET NAME_USER = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE ID_USER = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getLogin()
                , user.getEmail()
                , user.getBirthday()
                , id);
        log.info("Обновление данных пользователя в БД {}", user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> addFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с идентификатором %d в БД не найден.", userId)));

        User other = getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с идентификатором %d в БД не найден.", userId)));

        user.addFriends(friendId);
        String status = "Не подтвержденна";

        if (other.getFriends().contains(userId)) {
            status = "Подтвержденна";
            String sqlQuery = "UPDATE FRIENDS SET STATUS = ? WHERE USER_ID = ? AND OTHER_ID = ?";
            jdbcTemplate.update(sqlQuery, status, friendId, userId);
            log.info("Обновление статуса дружбы у пользователей ID {} / ID {}", userId, friendId);
        }

        if (user.getFriends().contains(friendId)) {
            String sqlQuery = "INSERT INTO FRIENDS (USER_ID, OTHER_ID, STATUS) VALUES(?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId, status);
            log.info("Пользователь ID {} добавил в друзья ID {}", userId, friendId);
        }

        return Optional.of(user);
    }
}
