package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
@Component
public interface UserStorage {
    Collection<User> allUser();

    User addUser(User user);

    User updateUser(User user);
}
