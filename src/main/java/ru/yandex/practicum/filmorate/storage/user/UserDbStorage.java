package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> allUser() {
        return null;
    }

    /*@Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (NAME_USER, LOGIN, EMAIL, BIRTHDAY) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        return user;
    }*/

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (NAME_USER, LOGIN, EMAIL) VALUES ( ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
           // ps.setDate(4, (Date) user.getBirthday());
           // ps.setDate(4, new java.sql.Date(user.getBirthday().));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }


    @Override
    public User updateUser(User user) {
        return null;
    }
}
