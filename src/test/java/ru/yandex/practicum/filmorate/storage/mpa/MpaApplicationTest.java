package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaApplicationTest {
    private final MpaStorage mpaStorage;

    @Test
    public void testMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void testAllMpa() {
        List<Optional<Mpa>> allMpa = List.of(Optional.of(new Mpa(1, "G")),
                Optional.of(new Mpa(2, "PG")),
                Optional.of(new Mpa(3, "PG-13")),
                Optional.of(new Mpa(4, "R")),
                Optional.of(new Mpa(5, "NC-17")));

        List<Optional<Mpa>> mpaOptional = mpaStorage.allMpa();
        assertThat(mpaOptional).isEqualTo(allMpa);
    }
}
