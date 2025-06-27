package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserValidator {

    public static void validate(User user) {
        validateEmail(user.getEmail());
        validateLogin(user.getLogin());
        validateBirthday(user.getBirthday());
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    private static void validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new ValidationException("логин не может быть пустым");
        }
        if (login.contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы");
        }
    }

    private static void validateBirthday(LocalDate birthday) {
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}