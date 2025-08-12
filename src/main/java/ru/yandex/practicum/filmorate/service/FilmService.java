package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final ValidationService validationService;

    public Collection<Film> findAll() {
        log.info("Попытка получения всех фильмов");
        return filmRepository.findAll();
    }

    public Film getFilmById(Long filmId) {
        log.info("Попытка получения фильма по ID: {}", filmId);
        validationService.validateFilmExists(filmId);
        return filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
    }

    public Film create(Film film) {
        log.info("Попытка создания фильма: {}", film.getName());
        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        validationService.validateFilmForCreate(film);
        Film createdFilm = filmRepository.create(film);
        log.info("Создан фильм с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film update(Film film) {
        log.info("Попытка обновления фильма с ID: {}", film.getId());
        Film existingFilm = filmRepository.getFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (film.getGenres() == null) {
            film.setGenres(existingFilm.getGenres());
        } else if (film.getGenres().isEmpty()) {
            film.setGenres(new HashSet<>());
        }
        validationService.validateFilmForUpdate(film);
        Film updatedFilm = filmRepository.update(film);
        log.info("Фильм с ID {} обновлен", film.getId());
        return updatedFilm;
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Попытка получения популярных фильмов в количестве {} штук", count);
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть положительным числом.");
        }
        return filmRepository.getPopularFilms(count);
    }

    public void delete(Long filmId) {
        boolean deleted = filmRepository.delete(filmId);
        if (!deleted) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
    }
}
