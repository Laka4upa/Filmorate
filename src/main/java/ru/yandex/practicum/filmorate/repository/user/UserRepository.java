package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Collection<User> findAll();

    Optional<User> getUserById(Long id);

    boolean delete(Long id);
}
