package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
//@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<Optional<User>> allUser() {
        return userStorage.allUser();
    }

    public Optional<User> addUser(User user) {
        return Optional.of(userStorage.addUser(user)
                .orElseThrow(() -> new UserNotFoundException("Ошибка добавления пользователя")));
    }

    public Optional<User> updateUser(User user) {
        return Optional.of(userStorage.updateUser(user)
                .orElseThrow(() -> new UserNotFoundException("Ошибка обновления пользователя")));
    }

    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id))));

        /*return userStorage.allUser().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));*/
    }

    public Optional<User> addFriends(Integer userId, Integer friendId) {
        Optional<User> user = getUserById(userId);
        Optional<User> friendsUser = getUserById(friendId);

        //user.addFriends(friendId);
        //friendsUser.addFriends(userId);

        return user;
    }

    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        Optional<User> user = getUserById(userId);
       Optional<User> friendsUser = getUserById(friendId);

        //user.removeFriends(friendsUser.getId());
        //friendsUser.removeFriends(user.getId());
        return user;
    }

    public List<User> allFriends(Integer userId) {
        Optional<User> user = getUserById(userId);
        return  null;//user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        Optional<User> user = getUserById(userId);
        Optional<User> friendsUser = getUserById(otherId);

        return null; /*user.getFriends().stream()
                .filter(f -> friendsUser.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList());*/
    }
}
