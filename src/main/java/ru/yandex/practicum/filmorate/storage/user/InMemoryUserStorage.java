/*
package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int userIid = 0;

    @Override
    public List<Optional<User>> allUser() {
        return users.values().stream().map(Optional::of)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> addUser(User user) {
        userIid++;
        user.setId(userIid);
        users.put(userIid, user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            return Optional.of(user);
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(users.values().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id))));
    }

    public Optional<User> addFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId).get();
        User friend = getUserById(friendId).get();
        user.addFriends(friendId);
        return Optional.of(user);
    }

    public Optional<User> deleteFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId).get();
        User friend = getUserById(friendId).get();
        user.removeFriends(friendId);
        return Optional.of(user);
    }

    public List<Optional<User>> allFriends(Integer userId) {
        User user = getUserById(userId).get();
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<Optional<User>> mutualFriends(Integer userId, Integer otherId) {
        User user = getUserById(userId).get();
        User friendsUser = getUserById(otherId).get();

        return user.getFriends().stream()
                .filter(f -> friendsUser.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
*/
