package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@AllArgsConstructor
@Data
public class User {
    private int id;

    private final Set<Integer> friends = new HashSet<>();

    @Email(message = "Не верный адрес Email")
    @NotNull(message = "Email = null")
    private String email;

    @NotBlank(message = "Login = NotBlank")
    private String login;

    private String name;

    @NotNull(message = "Birthday = null")
    @PastOrPresent(message = "Birthday из будущего")
    private LocalDate birthday;

//    public User(int id, String email, String login, String name, LocalDate birthday) {
//        this.id = id;
//        this.email = email;
//        this.login = login;
//        this.name = name;
//        this.birthday = birthday;
//    }

//    public User(String email, String login, String name, LocalDate birthday) {
//        this.email = email;
//        this.login = login;
//        this.name = name;
//        this.birthday = birthday;
//    }

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void removeFriends(Integer id) {
        friends.remove(id);
    }
}
