package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> allUser() {
        return userStorage.allUser();
    }

    public Optional<User> addUser(User user) {
        return userStorage.addUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        return userStorage.allUser().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    public User addFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friendsUser = getUserById(friendId);

        user.addFriends(friendId);
        friendsUser.addFriends(userId);

        return user;
    }

    public User deleteFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friendsUser = getUserById(friendId);

        user.removeFriends(friendsUser.getId());
        friendsUser.removeFriends(user.getId());
        return user;
    }

    public List<User> allFriends(Integer userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        User user = getUserById(userId);
        User friendsUser = getUserById(otherId);

        return user.getFriends().stream()
                .filter(f -> friendsUser.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
