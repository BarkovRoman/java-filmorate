package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
//@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<Optional<User>> allUser() {
        return userStorage.allUser();
    }

    public Optional<User> addUser(User user) {
        return userStorage.addUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public Optional<User> addFriends(Integer userId, Integer friendId) {
        return userStorage.addFriends(userId, friendId);
    }

    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        return userStorage.deleteFriends(userId, friendId);
    }

    public List<Optional<User>> allFriends(Integer userId) {
        return userStorage.allFriends(userId);
    }

    public List<Optional<User>> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        return userStorage.mutualFriends(userId, otherId);
    }
}
