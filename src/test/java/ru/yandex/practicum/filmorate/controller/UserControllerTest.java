package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = User.builder()
                .email("email@yandex.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1988,04,18))
                .build();
    }

    @Test
    void create_shouldAdduser() {
        User createdUser = userController.create(user);

        assertNotNull(createdUser.getId());
        assertEquals(1, userController.findAll().size());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void create_shouldUseLoginAsNameWhenNameIsEmpty() {
        user.setName("");
        User createdUser = userController.create(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void create_shouldUseLoginAsNameWhenNameIsNull() {
        user.setName(null);
        User createdUser = userController.create(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void create_shouldThrowWhenEmailIsBlank(String email) {
        user.setEmail(email);
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void create_shouldThrowWhenLoginIsBlank(String login) {
        user.setLogin(login);
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_shouldThrowWhenLoginContainsWhitespace() {
        user.setLogin("login with space");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_shouldThrowWhenBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void update_shouldThrowWhenIdIsNull() {
        assertThrows(ValidationException.class, () -> userController.update(user));
    }

    @Test
    void update_shouldUpdateExistingUser() {
        User createdUser = userController.create(user);
        User updatedUser = User.builder()
                .id(createdUser.getId())
                .email("updated@email.com")
                .login("updatedLogin")
                .name("Updated Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User result = userController.update(updatedUser);

        assertEquals("updated@email.com", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("Updated Name", result.getName());
        assertEquals(createdUser.getId(), result.getId());
    }
}