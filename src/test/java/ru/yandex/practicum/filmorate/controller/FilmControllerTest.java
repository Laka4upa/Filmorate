package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private static Validator validator;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWithCorrectData() {
        Film film = Film.builder()
                .name("Valid Film")
                .description("Valid description")
                .releaseDate(CINEMA_BIRTHDAY)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Нет нарушений при корректных данных");
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Film film = Film.builder()
                .name(" ")
                .description("Description")
                .releaseDate(CINEMA_BIRTHDAY)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при пустом названии");
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        String longDescription = "a".repeat(201);
        Film film = Film.builder()
                .name("Film")
                .description(longDescription)
                .releaseDate(CINEMA_BIRTHDAY)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при длинном описании");
        assertEquals("Максимальная длина описания — 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void shouldAcceptExactly200CharsDescription() {
        String exactLengthDescription = "a".repeat(200);
        Film film = Film.builder()
                .name("Film")
                .description(exactLengthDescription)
                .releaseDate(CINEMA_BIRTHDAY)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Нет нарушений при описании ровно 200 символов");
    }

    @Test
    void shouldFailWhenDurationNotPositive() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(CINEMA_BIRTHDAY)
                .duration(0)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при неположительной продолжительности");
        assertEquals("Продолжительность фильма должна быть положительной", violations.iterator().next().getMessage());
    }
}