package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWithCorrectData() {
        User user = User.builder()
                .email("valid@email.com")
                .login("validlogin")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Нет нарушений при корректных данных");
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsEmpty() {
        User user = User.builder()
                .email("valid@email.com")
                .login("validlogin")
                .name("")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        assertNull(user.getName(), "Имя должно быть null или пустым для подстановки логина");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "invalid", "invalid@", "@invalid"})
    void shouldFailWhenEmailInvalid(String email) {
        User user = User.builder()
                .email(email)
                .login("validlogin")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Должны быть нарушения при невалидном email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "login with spaces"})
    void shouldFailWhenLoginInvalid(String login) {
        User user = User.builder()
                .email("valid@email.com")
                .login(login)
                .birthday(LocalDate.now().minusYears(20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Должны быть нарушения при невалидном логине");
    }

    @Test
    void shouldFailWhenBirthdayInFuture() {
        User user = User.builder()
                .email("valid@email.com")
                .login("validlogin")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Одно нарушение при дате рождения в будущем");
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}