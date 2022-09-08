package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    private static final LocalDate BIRTHDAY = LocalDate.now();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserController userController;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void userCreate() throws Exception {
        User user = new User(1,"mail@mail.ru", "login", "name", BIRTHDAY);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    public void userCreateName() throws Exception {
        User user = new User(1,"mail@mail.ru", "login", "", BIRTHDAY);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("login"));
    }

    @Test
    public void userCreateEmail() throws Exception {
        User user = new User(1,"mail.ru", "login", "name", BIRTHDAY);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void userCreateBirthday() throws Exception {
        User user = new User(1,"mail@mail.ru", "login", "name", BIRTHDAY.plusDays(1));
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void userUpdate() throws Exception {
        User user = new User(1,"mail@mail.ru", "login", "name", BIRTHDAY);
        User user1 = new User(1,"mail@mail.ru", "login", "newName", BIRTHDAY);
        userController.create(user);
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(user1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("newName"));
    }

    @Test
    public void userUpdateId() throws Exception {
        User user = new User(1,"mail@mail.ru", "login", "name", BIRTHDAY);
        User user1 = new User(-1,"mail@mail.ru", "login", "newName", BIRTHDAY);
        userController.create(user);
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(user1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void userCreateLogin() throws Exception {
        User user = new User(1,"mail@mail.ru", "login login", "", BIRTHDAY);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }
}
