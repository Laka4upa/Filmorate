package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users - попытка добавления пользователя: {}", user.getEmail());
        if (users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))) {
            log.error("Такой email уже существует: {}", user.getEmail());
            throw new AlreadyExistsException("Email уже используется.");
        }
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использован логин: {}", user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        log.info("Пользователь создан ID={}", user.getId());
        return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            log.info("UPD: не указан Id при поиске пользователя");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());
        if (!user.getEmail().equalsIgnoreCase(oldUser.getEmail()) &&
                users.values()
                        .stream()
                        .anyMatch(users1 -> users1.getEmail().equalsIgnoreCase(user.getEmail()))) {
            log.error("Такой email уже существует: {}", user.getEmail());
            throw new AlreadyExistsException("Email уже используется.");
        }
        if (users.containsKey(user.getId())) {
            user.setFriends(users.get(user.getId()).getFriends());
        }
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());
        log.info("Пользователь {} обновлён", user.getLogin());
        return oldUser;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
