package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmValidator {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public static void validate(Film film) {
        validateName(film.getName());
        validateDescription(film.getDescription());
        validateReleaseDate(film.getReleaseDate());
        validateDuration(film.getDuration());
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
    }

    private static void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    private static void validateDuration(Integer duration) {
        if (duration != null && duration <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }
    }
}