package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class, UserRowMapper.class})
class JdbcUserRepositoryIntegrationTest {

    private final JdbcUserRepository userRepository;

    private User createSampleUser(String email, String login) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }

    @Test
    void createAndFindUser() {
        User saved = userRepository.create(createSampleUser("a@mail.com", "login1"));
        Optional<User> found = userRepository.getUserById(saved.getId());

        assertThat(found)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getEmail()).isEqualTo("a@mail.com");
                    assertThat(u.getLogin()).isEqualTo("login1");
                });
    }

    @Test
    void updateUser() {
        User saved = userRepository.create(createSampleUser("b@mail.com", "login2"));
        saved.setName("Updated Name");
        userRepository.update(saved);

        Optional<User> found = userRepository.getUserById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Name");
    }

    @Sql(statements = "DELETE FROM users")
    @Test
    void findAllUsers() {
        userRepository.create(createSampleUser("c1@mail.com", "login3"));
        userRepository.create(createSampleUser("c2@mail.com", "login4"));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void deleteUser() {
        User saved = userRepository.create(createSampleUser("d@mail.com", "login5"));
        userRepository.delete(saved.getId());

        Optional<User> found = userRepository.getUserById(saved.getId());
        assertThat(found).isEmpty();
    }
}
