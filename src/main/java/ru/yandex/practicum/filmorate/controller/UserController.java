package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    //создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // проверяем выполнение необходимых условий
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, будет использован логин: {}", user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь с id: {}", user.getId());
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        // проверяем необходимые условия
        if (user.getId() == null) {
            log.info("UPD:не указан Id при поиске пользователя");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        UserValidator.validate(user);
        User oldUser = users.get(user.getId());
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());
        log.info("Пользователь {} обновлён", user.getLogin());
        return oldUser;
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
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
