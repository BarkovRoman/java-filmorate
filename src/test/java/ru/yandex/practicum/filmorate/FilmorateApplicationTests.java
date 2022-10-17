package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
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
    public void testFindAllUser() {
        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", BIRTHDAY);
        userStorage.addUser(user1);
        List<Optional<User>> userOptional = userStorage.allUser();
        assertThat(userOptional.get(0))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testUpdateUser() {
        User user1 = new User(1, "yandex.@yandex.ru", "dolore", "Nick Name", BIRTHDAY);
        userStorage.addUser(user1);
        userStorage.updateUser(new User(1, "y.@yandex.ru", "dolore", "Nick Name", BIRTHDAY));
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "y.@yandex.ru")
                );

    }

    @Test
    public void testAddFriendsUser() {

        userStorage.addFriends(1, 2);
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).contains(2, 2)
                );
    }

    @Test
    public void testSizeFriendsUser() {
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(2)
                );

        Optional<User> userOptionalFriends = userStorage.getUserById(2);
        assertThat(userOptionalFriends)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(1)
                );
    }

    @Test
    public void testDeleteFriendsUser() {
        User user1 = new User(1, "y.@yandex.ru", "dolore1", "Nick Name1", BIRTHDAY);
        userStorage.addUser(user1);

        userStorage.deleteFriends(1, 2);
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(0)
                );
    }

    @Test
    public void testMutualFriendsUser() {
        userStorage.addFriends(1,3);
        userStorage.addFriends(2,3);
        userStorage.mutualFriends(1,2);

        List<Optional<User>> userOptional = userStorage.mutualFriends(1,2);
        assertThat(userOptional).hasSize(1);
        assertThat(userOptional.get(0))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 3)
                );
    }




}
