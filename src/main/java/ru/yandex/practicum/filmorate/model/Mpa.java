package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder(toBuilder = true)
public class Mpa {
    private Long id;
    @NotBlank(message = "Рейтинг не может быть пустым")
    private String name;
}
