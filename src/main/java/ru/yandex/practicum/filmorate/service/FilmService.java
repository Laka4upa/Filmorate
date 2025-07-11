package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getWhoLiked().size(), f1.getWhoLiked().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film.getWhoLiked().contains(userId)) {
            throw new AlreadyExistsException("Пользователь уже поставил лайк этому фильму");
        }

        film.getWhoLiked().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film == null || user == null) {
            throw new NotFoundException("Фильм или пользователь не найден");
        }
        if (film.getWhoLiked() == null || !film.getWhoLiked().contains(userId)) {
            throw new NotFoundException("Лайк не найден");
        }
        film.getWhoLiked().remove(userId);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }
}
