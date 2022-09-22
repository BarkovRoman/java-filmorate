package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(Integer id) {
        return userStorage.allUser().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id )));
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

    public Collection<User> allFriends(Integer userId) {
        User user = getUserById(userId);

        return userStorage.allUser().stream()
                .filter(f -> user.getFriends().contains(f.getId()))
                .collect(Collectors.toList());
    }

    public Collection<User> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        User user = getUserById(userId);
        User friendsUser = getUserById(otherId);

        return userStorage.allUser().stream()
                .filter(f -> user.getFriends().contains(f.getId()))
                .filter(f -> friendsUser.getFriends().contains(f.getId()))
                .collect(Collectors.toList());
    }
}
