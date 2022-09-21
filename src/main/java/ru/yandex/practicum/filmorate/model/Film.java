package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    private Set<Integer> like;

    @NotBlank(message = "Name = NotBlank")
    private String name;

    @Size(max = 200, message = "description > 200")
    private String description;

    @NotNull(message = "releaseDate = null")
    private LocalDate releaseDate;

    @Positive(message = "duration = null")
    private int duration;

    public void addLike(Integer id) {
        like.add(id);
    }

    public void removeLike(Integer id) {
        like.remove(id);
    }
}
