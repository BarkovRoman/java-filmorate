package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchRuntimeException;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private static final LocalDate BIRTHDAY = LocalDate.now().minusDays(2);

    @Test
    public void testFindUserById() {
        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", BIRTHDAY);
        userStorage.addUser(user1);
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindUserByIdNoId() {
        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", BIRTHDAY);
        userStorage.addUser(user1);
        Optional<User> userOptional = userStorage.getUserById(-1);
        catchRuntimeException(UserNotFoundException)
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

    }
}
