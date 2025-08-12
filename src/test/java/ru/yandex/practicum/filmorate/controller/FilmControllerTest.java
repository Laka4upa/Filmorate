package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    private Validator validator;
    private Film film;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    private JdbcGenreRepository genreRepository;

    @Autowired
    private JdbcMpaRepository mpaRepository;

    private Genre existingGenre;
    private Mpa existingMpa;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(CINEMA_BIRTHDAY);
        film.setDuration(120);
        existingGenre = genreRepository.findAllGenres().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Жанры не найдены в базе"));
        film.setGenres(Set.of(existingGenre));
        existingMpa = mpaRepository.findAllMpa().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("MPA рейтинги не найдены в базе"));

        film.setMpa(existingMpa);
    }

    @Test
    void shouldPassValidationWithCorrectData() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Нет нарушений при корректных данных");
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при пустом названии");
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        film.setDescription("a".repeat(201));        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при длинном описании");
        assertEquals("Максимальная длина описания — 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void shouldAcceptExactly200CharsDescription() {
        film.setDescription("a".repeat(200));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Нет нарушений при описании ровно 200 символов");
    }

    @Test
    void shouldFailWhenDurationNotPositive() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Одно нарушение при неположительной продолжительности");
        assertEquals("Продолжительность фильма должна быть положительной", violations.iterator().next().getMessage());
    }
}