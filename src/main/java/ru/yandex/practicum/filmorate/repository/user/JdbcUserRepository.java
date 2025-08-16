package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRepository extends BaseRepository<User> implements UserRepository {
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users ORDER BY user_id";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = :userId";
    private static final String INSERT_USERS_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            "VALUES (:email, :login, :name, :birthday)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = :email, login = :login, name = :name, " +
            "birthday = :birthday WHERE user_id = :userId";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = :userId";

    public JdbcUserRepository(NamedParameterJdbcOperations jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());

        long id = insert(INSERT_USERS_QUERY, params, "user_id");
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        params.put("userId", user.getId());

        update(UPDATE_USER_QUERY, params);
        return user;
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_USERS_QUERY, new HashMap<>());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", id);
        return findOne(FIND_USER_BY_ID_QUERY, params);
    }

    @Override
    public boolean delete(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", id);
        return delete(DELETE_USER_QUERY, params);
    }
}

