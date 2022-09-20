package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userIid = 0;

    public Collection<User> allUser() {
        return users.values();
    }

    public User addUser(User user) {
        validation(user);
        userIid++;
        user.setId(userIid);
        users.put(userIid, user);
        return user;
    }

    public User updateUser(User user) {
        validation(user);
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", id ));
        }
    }

    private void validation(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
