package ru.yandex.practicum.filmorate.repository.friend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JdbcFriendRepository.class, JdbcUserRepository.class, UserRowMapper.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcFriendRepositoryIntegrationTest {

    @Autowired
    private JdbcFriendRepository friendRepository;

    @Autowired
    private JdbcUserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long userId1;
    private Long userId2;
    private Long userId3;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM users");
        userId1 = createUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        userId2 = createUser("user2@example.com", "user2", "User Two", LocalDate.of(1991, 2, 2));
        userId3 = createUser("user3@example.com", "user3", "User Three", LocalDate.of(1992, 3, 3));
    }

    @Test
    void testAddFriend_increasesFriendsCount() {
        int countBefore = friendRepository.getFriends(userId1).size();

        friendRepository.addFriend(userId1, userId2);

        int countAfter = friendRepository.getFriends(userId1).size();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void testRemoveFriend() {
        friendRepository.addFriend(userId1, userId2);

        friendRepository.removeFriend(userId1, userId2);

        assertThat(friendRepository.hasFriendship(userId1, userId2)).isFalse();
    }

    @Test
    void testGetFriends() {
        friendRepository.addFriend(userId1, userId2);
        friendRepository.addFriend(userId1, userId3);

        List<User> friends = friendRepository.getFriends(userId1);

        assertThat(friends)
                .hasSize(2)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(userId2, userId3);
    }

    @Test
    void testGetCommonFriends() {
        // userId3 — общий друг
        friendRepository.addFriend(userId1, userId3);
        friendRepository.addFriend(userId2, userId3);

        List<User> commonFriends = friendRepository.getCommonFriends(userId1, userId2);

        assertThat(commonFriends)
                .hasSize(1)
                .extracting(User::getId)
                .containsExactly(userId3);
    }

    private Long createUser(String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return userRepository.create(user).getId();
    }
}