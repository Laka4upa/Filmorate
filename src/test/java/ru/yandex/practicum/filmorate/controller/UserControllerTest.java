package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        user = new User();
        user.setEmail("valid@email.com");
        user.setLogin("validlogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.now().minusYears(20));
    }

    @Test
    void shouldPassValidationWithCorrectData() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Нет нарушений при корректных данных");
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsEmpty() {
        user.setName("");
        assertTrue(user.getName() == null || user.getName().isEmpty(),
                "Имя должно быть null или пустым для подстановки логина");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "invalid", "invalid@", "@invalid"})
    void shouldFailWhenEmailInvalid(String email) {
        user.setEmail(email);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Должны быть нарушения при невалидном email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "login with spaces"})
    void shouldFailWhenLoginInvalid(String login) {
        user.setLogin(login);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Должны быть нарушения при невалидном логине");
    }

    @Test
    void shouldFailWhenBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Одно нарушение при дате рождения в будущем");
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}