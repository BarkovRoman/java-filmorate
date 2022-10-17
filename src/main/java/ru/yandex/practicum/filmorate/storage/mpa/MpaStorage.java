package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Component
public interface MpaStorage {
    List<Optional<Mpa>> allMpa();

    Optional<Mpa> getMpaById(Integer id);
}
