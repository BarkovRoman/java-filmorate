package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.allUser();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validation(user);
        return userService.addUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validation(user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")  // Добавление в друзья
    public User addFriends(@PathVariable("id") Integer userId, @PathVariable Integer friendId) {
        return userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // Удаление из друзей
    public User deleteFriends(@PathVariable("id") Integer userId, @PathVariable Integer friendId) {
        return userService.deleteFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")  // Возвращаем список пользователей, являющихся его друзьями
    public List<User> friends(@PathVariable Integer id) {
        return userService.allFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем
    public List<User> mutualFriends(@PathVariable("id") Integer userId, @PathVariable Integer otherId) {
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
