package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<Optional<User>> findAll() {
        return userService.allUser();
    }

    @PostMapping
    public Optional<User> create(@Valid @RequestBody User user) {
        validation(user);
        log.info("Создан пользователь {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> put(@Valid @RequestBody User user) {
        validation(user);
        log.info("Обновление данных пользователя {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        log.info("GET /users/{}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")  // Добавление в друзья
    public Optional<User> addFriends(@PathVariable("id") Integer userId, @PathVariable Integer friendId) {
        log.info("Пользователь ID {} добовляет ID {}",userId, friendId);
        return userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // Удаление из друзей
    public Optional<User> deleteFriends(@PathVariable("id") Integer userId, @PathVariable Integer friendId) {
        log.info("Пользователь ID {} удаляет ID {}",userId, friendId);
        return userService.deleteFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")  // Возвращаем список пользователей, являющихся его друзьями
    public List<Optional<User>> friends(@PathVariable Integer id) {
        log.info("Просмотр списка друзей пользователя ID {}", id);
        return userService.allFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем
    public List<Optional<User>> mutualFriends(@PathVariable("id") Integer userId, @PathVariable Integer otherId) {
        log.info("Просмотр списка общих друзей пользователя ID {} и ID {}", userId, otherId);
        return userService.mutualFriends(userId, otherId);
    }

    private void validation(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
