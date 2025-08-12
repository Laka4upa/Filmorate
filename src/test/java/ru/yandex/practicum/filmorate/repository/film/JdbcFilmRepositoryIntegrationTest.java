package ru.yandex.practicum.filmorate.repository.film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        JdbcFilmRepository.class,
        FilmRowMapper.class,
        JdbcGenreRepository.class,
        GenreRowMapper.class,
        JdbcMpaRepository.class,
        MpaRatingRowMapper.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // <--- ключевая строчка
public class JdbcFilmRepositoryIntegrationTest {

    @Autowired
    private JdbcFilmRepository filmRepository;

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2020,1,1))
                .duration(120)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(new HashSet<>(Arrays.asList(
                        Genre.builder().id(1L).name("Комедия").build(),
                        Genre.builder().id(2L).name("Драма").build()
                )))
                .build();

        Film createdFilm = filmRepository.create(film);

        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getId()).isNotNull().isPositive();
        assertThat(createdFilm.getName()).isEqualTo("Test Film");
        assertThat(createdFilm.getDescription()).isEqualTo("Test Description");
        assertThat(createdFilm.getReleaseDate()).isEqualTo(LocalDate.of(2020,1,1));
        assertThat(createdFilm.getDuration()).isEqualTo(120);
        assertThat(createdFilm.getMpa().getId()).isEqualTo(1L);
        assertThat(createdFilm.getGenres()).hasSize(2);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> foundFilm = filmRepository.getFilmById(1L);

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getId()).isEqualTo(1L);
        assertThat(foundFilm.get().getName()).isEqualTo("Test Film 1");
        assertThat(foundFilm.get().getDescription()).isEqualTo("Test Description 1");
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmRepository.findAll();

        assertThat(films).hasSize(2);
        assertThat(films).extracting(Film::getName)
                .containsExactlyInAnyOrder("Test Film 1", "Test Film 2");
    }

    @Test
    public void testUpdateFilm() {
        Film updatedFilm = Film.builder()
                .id(1L)
                .name("Updated Film")
                .description("Updated Description")
                .releaseDate(LocalDate.of(2021, 1, 1))
                .duration(150)
                .mpa(Mpa.builder().id(2L).name("PG").build())
                .genres(new HashSet<>(Collections.singletonList(
                        Genre.builder().id(1L).name("Комедия").build()
                )))
                .build();

        Film result = filmRepository.update(updatedFilm);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Updated Film");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getReleaseDate()).isEqualTo(LocalDate.of(2021, 1, 1));
        assertThat(result.getDuration()).isEqualTo(150);
        assertThat(result.getMpa().getId()).isEqualTo(2L);
        assertThat(result.getGenres()).hasSize(1);
    }

    @Test
    public void testDeleteFilm() {
        boolean deleted = filmRepository.delete(1L);

        assertThat(deleted).isTrue();
        assertThat(filmRepository.getFilmById(1L)).isEmpty();
    }

    @Test
    public void testDeleteNonExistentFilm() {
        boolean deleted = filmRepository.delete(999L);
        assertThat(deleted).isFalse();
    }

    @Test
    public void testGetPopularFilms() {
        Collection<Film> popularFilms = filmRepository.getPopularFilms(2);

        assertThat(popularFilms).hasSize(2);
        assertThat(popularFilms)
                .extracting(Film::getName)
                .contains("Test Film 1", "Test Film 2");
    }
}
