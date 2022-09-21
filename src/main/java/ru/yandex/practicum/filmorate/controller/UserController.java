package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }
    @GetMapping
    public Collection<User> findAll() {
        return userStorage.allUser();
    }


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
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
    public Collection<User> friends(@PathVariable Integer id) {
        return userService.allFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем
    public Collection<User> mutualFriends(@PathVariable("id") Integer userId, @PathVariable Integer otherId) {
        return userService.mutualFriends(userId, otherId);
    }
}
