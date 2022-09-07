package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;


@AutoConfigureMockMvc
@SpringBootTest
class FilmControllerTest {
    private final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FilmController filmController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void filmCreate() throws Exception {
        Film film = new Film(0, "filmCreate", RandomString.make(200), RELEASE_DATA, 100);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("filmCreate"));
    }

    @Test
    public void filmCreateName() throws Exception {
        Film film = new Film(2, "", RandomString.make(200), RELEASE_DATA, 100);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void filmCreateDescription() throws Exception {
        Film film = new Film(2, "filmCreateDescription", RandomString.make(201), RELEASE_DATA, 100);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void filmCreateReleaseDate() throws Exception {
        Film film = new Film(2, "filmCreateReleaseDate", RandomString.make(200), RELEASE_DATA.minusDays(1), 100);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void filmCreateDuration() throws Exception {
        Film film = new Film(2, "filmCreateDuration", RandomString.make(200), RELEASE_DATA, 0);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void filmUpdate() throws Exception {
        Film film = new Film(1, "filmUpdate", RandomString.make(100), RELEASE_DATA, 120);
        Film film1 = new Film(1, "newFilmUpdate", RandomString.make(100), RELEASE_DATA, 120);
        filmController.create(film);

        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("newFilmUpdate"));
    }

    @Test
    public void filmUpdateId() throws Exception {
        Film film = new Film(1, "filmUpdateId", RandomString.make(100), RELEASE_DATA, 120);
        Film film1 = new Film(-1, "newFilmUpdateId", RandomString.make(100), RELEASE_DATA, 120);
        filmController.create(film);

        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
