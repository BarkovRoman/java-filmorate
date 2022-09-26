package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> allUser() {
        return userStorage.allUser();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(userStorage.allUser().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id))));
    }

    public Optional<User> addFriends(Integer userId, Integer friendId) {
        Optional<User> user = getUserById(userId);
        Optional<User> friendsUser = getUserById(friendId);

        user.ifPresent(u -> u.addFriends(friendId));
        friendsUser.ifPresent(u -> u.addFriends(userId));

        //user.addFriends(friendId);
        //friendsUser.addFriends(userId);
        //return user;
        return user;
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        /*User user = getUserById(userId);
        User friendsUser = getUserById(friendId);

        user.removeFriends(friendsUser.getId());
        friendsUser.removeFriends(user.getId());
        return user;*/

        getUserById(friendId).ifPresent(user1 -> user1.removeFriends(userId));
        getUserById(userId).ifPresent(user1 -> user1.removeFriends(friendId));

    }

    public List<Optional<User>> allFriends(Integer userId) {
        /*User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());*/

        Optional<User> user = getUserById(userId);

        return user.ifPresent(u -> u.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList()));

    }



    public List <User> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        Optional<User> user = getUserById(userId);
        Optional<User> friendsUser = getUserById(otherId);

        return user.ifPresent(u -> u.getFriends().stream()
                .filter(f -> friendsUser.ifPresent(p -> p.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList())));
    }
}
