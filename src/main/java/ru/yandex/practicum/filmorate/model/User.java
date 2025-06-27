package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    Long id;
    @Email @NotBlank(message = "электронная почта не может быть пустой и должна содержать символ @")
    String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы")
    String login;
    String name;
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    LocalDate birthday;
}
