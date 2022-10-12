package ru.yandex.practicum.filmorate.storage.user;

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
import ru.yandex.practicum.filmorate.model.User;

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
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> getUserById(Integer id) {
        String sql = "SELECT* FROM USERS WHERE ID_USER = ?";
        //String sql = "SELECT* FROM USERS u INNER JOIN FRIENDS f ON u.ID_USER = f.USER_ID WHERE ID_USER = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения User из базы данных");
        }
    }

    @Override
    public List<Optional<User>> allUser() {
        String sql = "SELECT* FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private Optional<User> makeUser(ResultSet rs) {
        try {
            int id = rs.getInt("ID_USER");
            String email = rs.getString("EMAIL");
            String login = rs.getString("LOGIN");
            String name = rs.getString("NAME_USER");
            LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
            User user = new User(id, email, login, name, birthday);
            friendsOfUser(user);
            log.info("Найден пользователь: {} ", user);
            return Optional.of(user);
        } catch (DataAccessException | SQLException e) {
            throw new DataBaseException("Ошибка получения User из базы данных");
        }
    }

    private void friendsOfUser(User user) {
        String sql = "SELECT OTHER_ID FROM FRIENDS WHERE USER_ID = ?";
        jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), user.getId()).forEach(user::addFriends);
    }

    private Integer makeFriends(ResultSet rs) {
        try {
            return rs.getInt("OTHER_ID");
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Friends из базы данных");
        }
    }

    @Override
    public Optional<User> addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (NAME_USER, LOGIN, EMAIL, BIRTHDAY) VALUES( ?, ?, ?, ?)";
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

        getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с идентификатором %d в БД не найден.", userId)));
        user.addFriends(friendId);

        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, OTHER_ID) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь ID {} добавил в друзья ID {}", userId, friendId);

        return Optional.of(user);
    }

    @Override
    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с идентификатором %d в БД не найден.", userId)));

        getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с идентификатором %d в БД не найден.", userId)));
        user.removeFriends(friendId);

        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND OTHER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь ID {} удалил из друзей ID {}", userId, friendId);

        return Optional.of(user);
    }

    @Override
    public List<Optional<User>> allFriends(Integer userId) {
        String sql = "SELECT OTHER_ID FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), userId).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<Optional<User>> mutualFriends(Integer userId, Integer otherId) {
        String sql = "SELECT OTHER_ID FROM FRIENDS WHERE USER_ID IN (?, ?)" +
                "GROUP BY OTHER_ID HAVING COUNT(OTHER_ID) > 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), userId, otherId).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
