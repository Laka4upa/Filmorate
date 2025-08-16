package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.like.LikeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ValidationService validationService;

    public void addLike(Long filmId, Long userId) {
        log.info("Попытка добавить лайк фильму {} от пользователя {}", filmId, userId);
        validationService.validateFilmAndUserIds(filmId, userId);
        validationService.validateFilmExists(filmId);
        validationService.validateUserExists(userId);
        likeRepository.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Попытка удалить лайк у фильма {} от пользователя {}", filmId, userId);
        validationService.validateFilmAndUserIds(filmId, userId);
        likeRepository.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }
}