package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    List<Optional<User>> allUser();

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(Integer id);

    Optional<User> addFriends(Integer userId, Integer friendId);
}
