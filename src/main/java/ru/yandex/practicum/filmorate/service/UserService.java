package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collections;
@Service
public class UserService {
    private final UserStorage userStorage;
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(User user, User friendsUser) {
        user.addFriends(friendsUser.getId());
        friendsUser.addFriends(user.getId());
        return user;
    }

    public User deleteFriends(User user, User friendsUser) {
        user.removeFriends(friendsUser.getId());
        friendsUser.removeFriends(user.getId());
        return user;
    }

    /*public Collection<User> mutualFriends (User user) {  // вывод списка общих друзей

        return userStorage.allUser().stream().filter(f -> f.getFriends().contains());
    }*/



}
