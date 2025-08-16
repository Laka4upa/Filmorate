package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    public void validateUserExists(Long userId) {
        if (userId == null) {
            throw new ValidationException("ID пользователя не может быть null");
        }
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    public void validateUsersExist(Long userId1, Long userId2) {
        if (userId1 == null || userId2 == null) {
            throw new ValidationException("ID пользователей не могут быть null");
        }
        validateUserExists(userId1);
        validateUserExists(userId2);
    }

    public void validateFilmForCreate(Film film) {
        validateFilmBasicInfo(film);
        validateMpa(film.getMpa());
        if (film.getGenres() != null) {
            validateGenres(film.getGenres());
        }
    }

    public void validateFilmForUpdate(Film film) {
        validateFilmBasicInfo(film);
        validateMpa(film.getMpa());
        if (film.getGenres() != null) {
            validateGenres(film.getGenres());
        }
    }

    // Общие методы валидации:
    private void validateFilmBasicInfo(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть null");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза обязательна для заполнения");
        }
        LocalDate firstFilmEver = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(firstFilmEver)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    private void validateMpa(Mpa mpa) {
        if (mpa == null || mpa.getId() == null) {
            throw new ValidationException("Фильм должен иметь рейтинг MPA");
        }
        validateMpaExists(mpa.getId());
    }

    private void validateGenres(Set<Genre> genres) {
        for (Genre genre : genres) {
            if (genre.getId() == null) {
                throw new ValidationException("Жанр должен иметь ID");
            }
            validateGenreExists(genre.getId());
        }
    }

    public void validateFilmExists(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("ID фильма не может быть null");
        }
        filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
    }

    public void validateGenreExists(Long genreId) {
        if (genreId == null) {
            throw new ValidationException("ID жанра не может быть null");
        }
        genreRepository.findGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с ID " + genreId + " не найден"));
    }

    public void validateMpaExists(Long mpaId) {
        if (mpaId == null) {
            throw new ValidationException("ID рейтинга не может быть null");
        }
        mpaRepository.findMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с ID " + mpaId + " не найден"));
    }

    public void validateFilmAndUserIds(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма и пользователя не могут быть null");
        }
        if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("ID должны быть положительными числами");
        }
    }
}
