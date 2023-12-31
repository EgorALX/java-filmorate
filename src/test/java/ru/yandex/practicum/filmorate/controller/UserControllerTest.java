package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
    public static final String PATH = "/users";
    @Autowired
    private MockMvc mockMvc;
    private UserController userController;
    private ValidateService validateService = new ValidateService();

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(getContentFromFile("controller/request/user.json")));
    }

    @Test
    void createTestNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user-Email-exception.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void validateTest() {
        User user = User.builder()
                .name("Name")
                .id(1L)
                .login("login")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        validateService.validateUser(user);
    }

    @Test
    void validateTestNegative() {
        User user = User.builder()
                .name("Name")
                .login("login")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateUser(user));
    }

    private String getContentFromFile(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
