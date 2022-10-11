package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return userStorage.addUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> getUserById(Integer id) {
        return userStorage.getUserById(id);

        /*return userStorage.allUser().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));*/
    }

    public Optional<User> addFriends(Integer userId, Integer friendId) {
        return userStorage.addFriends(userId, friendId);
    }

    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        return userStorage.deleteFriends(userId, friendId);
        /*Optional<User> user = getUserById(userId);
       Optional<User> friendsUser = getUserById(friendId);

       user.removeFriends(friendsUser.getId());
        friendsUser.removeFriends(user.getId());
        return user;*/
    }

    public List<Optional<User>> allFriends(Integer userId) {
        /*Optional<User> user = getUserById(userId);
        return  user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());*/
        return userStorage.allFriends(userId);
    }

    public List<Optional<User>> mutualFriends(Integer userId, Integer otherId) {  // вывод списка общих друзей
        return userStorage.mutualFriends(userId, otherId);



        /*Optional<User> user = getUserById(userId);
        Optional<User> friendsUser = getUserById(otherId);

        return null; user.getFriends().stream()
                .filter(f -> friendsUser.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList());*/
    }
}
