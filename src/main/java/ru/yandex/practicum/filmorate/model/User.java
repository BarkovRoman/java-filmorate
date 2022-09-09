package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;


@AllArgsConstructor
@Data
public class User {
    private int id;

    @Email(message = "Не верный адрес Email")
    private String email;

    @NotBlank(message = "Login = NotBlank")
    private String login;

    private String name;

    @NotNull(message = "Birthday = null")
    @PastOrPresent(message = "Birthday из будущего")
    private LocalDate birthday;

}
