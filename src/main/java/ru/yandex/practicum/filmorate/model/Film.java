package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter @Setter
public class Film {
    private int id;
    private final Set<Integer> like = new HashSet<>();

    @NotBlank(message = "Name = NotBlank")
    private String name;

    @Size(max = 200, message = "description > 200")
    private String description;

    @NotNull(message = "releaseDate = null")
    private LocalDate releaseDate;

    @Positive(message = "duration = null")
    private int duration;
    //private String genres;
    private String mpa;

    public Film(String name, String description, LocalDate releaseDate, int duration, String mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, String mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
    public void addLike(Integer id) {
        like.add(id);
    }
    public void removeLike(Integer id) {
        like.remove(id);
    }
}
