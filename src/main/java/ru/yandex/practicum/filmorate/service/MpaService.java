package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public List<Optional<Mpa>> allMpa() {
        return mpaDao.allMpa();
    }

    public Optional<Mpa> getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }
}
