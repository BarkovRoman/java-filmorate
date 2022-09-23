package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userIid = 0;

    @Override
    public List<User> allUser() {
        return List.copyOf(users.values());
    }

    @Override
    public User addUser(User user) {
        userIid++;
        user.setId(userIid);
        users.put(userIid, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", id ));
        }
    }
}
