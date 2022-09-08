package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody User user) {
        validation(user);
        id++;
        user.setId(id);
        users.put(id, user);
        log.debug("Добавлен пользователь {}, всего пользователей {}", user, users.size());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping
    public ResponseEntity<Object> put(@Valid @RequestBody User user) {
        validation(user);
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            log.debug("Обновление данных пользователя id {}, новые данные {}", user.getId(), user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            log.debug("пользователь с id {} не найден", user.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }

    private void validation(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка ввода логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
