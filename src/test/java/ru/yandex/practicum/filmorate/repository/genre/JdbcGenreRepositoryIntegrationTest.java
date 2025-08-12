package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcGenreRepository.class, GenreRowMapper.class})
class JdbcGenreRepositoryIntegrationTest {

    private final JdbcGenreRepository genreRepository;

    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = genreRepository.findAllGenres();

        assertThat(genres).isNotEmpty();
        assertThat(genres).allMatch(g -> g.getId() != null && g.getName() != null);
    }

    @Test
    void shouldFindGenreById_WhenGenreExists() {
        Optional<Genre> genreOpt = genreRepository.findGenreById(1L);

        assertThat(genreOpt).isPresent();
        assertThat(genreOpt.get().getId()).isEqualTo(1L);
        assertThat(genreOpt.get().getName()).isNotBlank();
    }

    @Test
    void shouldReturnEmptyOptional_WhenGenreDoesNotExist() {
        Optional<Genre> genreOpt = genreRepository.findGenreById(999L);

        assertThat(genreOpt).isEmpty();
    }

    @Test
    void shouldFindGenresByFilmId() {
        Set<Genre> genres = genreRepository.findGenreByFilmId(1L);

        assertThat(genres).isNotEmpty();
        assertThat(genres).allMatch(g -> g.getId() != null && g.getName() != null);
    }

    @Test
    void shouldReturnEmptySet_WhenFilmHasNoGenres() {
        Set<Genre> genres = genreRepository.findGenreByFilmId(999L);

        assertThat(genres).isEmpty();
    }
}
