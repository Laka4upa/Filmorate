package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public Collection<User> findAll() {
        log.info("Попытка получения списка всех пользователей.");
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        log.info("Попытка получения пользователя по ID: {}", userId);
        validationService.validateUserExists(userId);
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден."));
    }

    public User create(User user) {
        log.info("Попытка создания нового пользователя: email={}, login={}", user.getEmail(), user.getLogin());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User createdUser = userRepository.create(user);
        log.info("Создан пользователь с ID: {}", createdUser.getId());
        return createdUser;
    }

    public User update(User newUser) {
        log.info("Попытка обновления пользователя с ID: {}", newUser.getId());
        validationService.validateUserExists(newUser.getId());
        boolean userExists = userRepository.getUserById(newUser.getId()).isPresent();
        if (!userExists) {
            throw new NotFoundException("Пользователь с ID " + newUser.getId() + " не найден.");
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        User updatedUser = userRepository.update(newUser);
        log.info("Пользователь с ID {} обновлен", newUser.getId());
        return updatedUser;
    }

    public void delete(Long id) {
        boolean deleted = userRepository.delete(id);
        if (!deleted) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }
}
