package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Optional<Mpa>> allMpa() {
        return mpaStorage.allMpa();
    }

    public Optional<Mpa> getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }
}
