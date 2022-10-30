package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataBaseException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
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
@Component("UserDbStorage")
@RequiredArgsConstructor

public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> getUserById(Integer id) {
        String sql = "SELECT* FROM USERS WHERE id_user = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).get(0);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения User из базы данных");
        } catch (Throwable e) {
            throw new EntityNotFoundException(String.format("User № %d в БД не найден!", id));
        }
    }

    @Override
    public List<Optional<User>> allUser() {
        String sql = "SELECT* FROM USERS";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка User из базы данных");
        }
    }

    private Optional<User> makeUser(ResultSet rs) {
        try {
            int id = rs.getInt("id_user");
            String email = rs.getString("email");
            String login = rs.getString("login");
            String name = rs.getString("name_user");
            LocalDate birthday = rs.getDate("birthday").toLocalDate();
            User user = new User(id, email, login, name, birthday);

            friendsOfUser(user);
            log.info("Найден пользователь: {} ", user);

            return Optional.of(user);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения User из базы данных");
        }
    }

    private void friendsOfUser(User user) {
        String sql = "SELECT other_id FROM FRIENDS WHERE user_id = ?";
        try {
            jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), user.getId()).forEach(user::addFriends);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка ID друзей из базы данных");
        }
    }

    private Integer makeFriends(ResultSet rs) {
        try {
            return rs.getInt("other_id");
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка получения Friends из базы данных");
        }
    }

    @Override
    public Optional<User> addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (name_user, login, email, birthday) VALUES( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"ID_USER"});
                ps.setString(1, user.getName());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getEmail());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка добавления пользователя в БД");
        }
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Пользователь {} добавлен в БД", user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        int id = user.getId();
        getUserById(id);

        String sqlQuery = "UPDATE USERS SET name_user = ?, login = ?, email = ?, birthday = ? WHERE id_user = ?";
        try {
            jdbcTemplate.update(sqlQuery
                    , user.getName()
                    , user.getLogin()
                    , user.getEmail()
                    , user.getBirthday()
                    , id);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка обновления пользователя в БД");
        }
        log.info("Обновление данных пользователя в БД {}", user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> addFriends(Integer userId, Integer friendId) {
        Optional<User> user = getUserById(userId);
        getUserById(friendId);
        user.ifPresent(user1 -> user1.addFriends(friendId));

        String sqlQuery = "INSERT INTO FRIENDS (user_id, other_id) VALUES(?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка добавления друзей пользователя в БД");
        }
        log.info("Пользователь ID {} добавил в друзья ID {}", userId, friendId);

        return user;
    }

    @Override
    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        Optional<User> user = getUserById(userId);
        getUserById(friendId);
        user.ifPresent(user1 -> user1.removeFriends(friendId));

        String sqlQuery = "DELETE FROM FRIENDS WHERE user_id = ? AND other_id = ?";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка удаления друзей пользователя в БД");
        }
        log.info("Пользователь ID {} удалил из друзей ID {}", userId, friendId);

        return user;
    }

    @Override
    public List<Optional<User>> allFriends(Integer userId) {
        String sql = "SELECT other_id FROM FRIENDS WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), userId).stream()
                    .map(this::getUserById)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения друзей пользователя в БД");
        }
    }

    @Override
    public List<Optional<User>> mutualFriends(Integer userId, Integer otherId) {
        String sql = "SELECT other_id FROM FRIENDS WHERE user_id IN (?, ?)" +
                "GROUP BY other_id HAVING COUNT(other_id) > 1";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), userId, otherId).stream()
                    .map(this::getUserById)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DataBaseException("Ошибка получения списка общих друзей в БД");
        }
    }
}
