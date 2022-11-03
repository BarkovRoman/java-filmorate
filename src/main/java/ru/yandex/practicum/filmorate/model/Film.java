package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @NotNull(message = "mpa = null")
    private Mpa mpa;

    public void addLike(Integer id) {
        like.add(id);
    }

    public void removeLike(Integer id) {
        like.remove(id);
    }
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));   //(Comparator.comparing(Genre::getId));

    public void addGenre(Genre ganre) {
        genres.add(ganre);
    }
}
