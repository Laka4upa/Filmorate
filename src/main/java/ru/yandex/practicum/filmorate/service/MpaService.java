package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public Mpa findMpaById(Long mpaId) {
        return mpaRepository.findMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг с таким ID: " + mpaId + " не найден."));
    }

    public List<Mpa> findAllMpa() {
        return mpaRepository.findAllMpa();
    }
}