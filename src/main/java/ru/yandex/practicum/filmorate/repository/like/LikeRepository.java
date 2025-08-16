package ru.yandex.practicum.filmorate.repository.like;

import java.util.Set;

public interface LikeRepository {

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Set<Long> findLikesByFilmId(Long filmId);
}
