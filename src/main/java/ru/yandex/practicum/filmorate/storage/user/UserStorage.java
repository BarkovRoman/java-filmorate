package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.security.KeyException;
import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    List<User> allUser();

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);
}
