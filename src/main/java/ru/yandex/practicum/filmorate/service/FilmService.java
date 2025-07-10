package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Autowired FilmStorage filmStorage, @Autowired UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getWhoLiked().size(), f1.getWhoLiked().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);

        if (film.getWhoLiked() == null) {
            film.setWhoLiked(new HashSet<>());
        }

        if (film.getWhoLiked().contains(userId)) {
            throw new AlreadyExistsException("Пользователь уже поставил лайк этому фильму");
        }

        film.addLike(userId);
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

        film.removeLike(userId);
    }
}
