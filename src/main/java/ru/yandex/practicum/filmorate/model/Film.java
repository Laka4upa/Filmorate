package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Film {
    Long id;
    @NotBlank(message = "название не может быть пустым")
    String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive
    Integer duration;
}
