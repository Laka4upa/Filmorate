package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = Film.builder()
                .name("film1")
                .description("descriptionFilm1")
                .releaseDate(LocalDate.of(2000,12,12))
                .duration(100)
                .build();
    }

    @Test
    void create_shouldAddFilm() {
        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm.getId());
        assertEquals(1, filmController.findAll().size());
        assertEquals(film.getName(), createdFilm.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void create_shouldThrowWhenNameIsBlank(String name) {
        film.setName(name);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_shouldThrowWhenNameIsNull() {
        film.setName(null);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_shouldThrowWhenDescriptionOver200Chars() {
        String longDescription = "a".repeat(201);
        film.setDescription(longDescription);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_shouldAcceptDescriptionExactly200Chars() {
        String exactLengthDescription = "a".repeat(200);
        film.setDescription(exactLengthDescription);
        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void create_shouldThrowWhenReleaseDateBefore1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_shouldAcceptReleaseDateExactly1895_12_28() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> filmController.create(film));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void create_shouldThrowWhenDurationNotPositive(int duration) {
        film.setDuration(duration);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void update_shouldUpdateExistingFilm() {
        Film createdFilm = filmController.create(film);
        Film updatedFilm = Film.builder()
                .id(createdFilm.getId())  // сохраняем тот же ID
                .name("Updated Name")
                .description("Updated Description")
                .releaseDate(createdFilm.getReleaseDate())  // копируем неизменяемые поля
                .duration(createdFilm.getDuration())
                .build();
        Film result = filmController.update(updatedFilm);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(createdFilm.getId(), result.getId());
        // Проверяем, что неизменяемые поля остались прежними
        assertEquals(createdFilm.getReleaseDate(), result.getReleaseDate());
        assertEquals(createdFilm.getDuration(), result.getDuration());
    }

}