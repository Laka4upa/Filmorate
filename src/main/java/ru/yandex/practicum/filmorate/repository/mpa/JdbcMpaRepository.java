package ru.yandex.practicum.filmorate.repository.mpa;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcMpaRepository extends BaseRepository<Mpa> implements MpaRepository {

    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE mpa_id = :mpaId";

    public JdbcMpaRepository(NamedParameterJdbcOperations jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> findMpaById(Long mpaId) {
        Map<String, Object> params = new HashMap<>();
        params.put("mpaId", mpaId);
        return findOne(FIND_BY_ID_QUERY, params);
    }

    @Override
    public List<Mpa> findAllMpa() {
        return findMany(FIND_ALL_QUERY, new HashMap<>());
    }
}
