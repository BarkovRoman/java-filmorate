package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}