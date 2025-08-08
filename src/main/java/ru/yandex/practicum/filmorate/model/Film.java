package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @NotNull(message = "Дата релиза обязательна для заполнения")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private Set<Long> whoLiked = new HashSet<>();
    @NotEmpty(message = "Фильм должен содержать хотя бы один жанр")
    private Set<Genre> genres = new HashSet<>();
    @NotNull(message = "Рейтинг MPA не может быть пустой")
    private Mpa mpa;
}
