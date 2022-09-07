package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validation(user);
        id++;
        user.setId(id);
        users.put(id, user);
        log.debug("Добавлен пользователь {}, всего пользователей {}", user, users.size());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        validation(user);
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            log.debug("Обновление данных пользователя id {}, новые данные {}", user.getId(), user);
            return user;
        } else {
            log.debug("пользователь с id {} не найден", user.getId());
            throw new ValidationException("Не верный ID");
        }
    }

    private void validation(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка ввода email");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка ввода логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка ввода даты рождения. Вы ввели {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть позже текущей даты");
        }
    }
}
