package com.yoshidainasaku.output.bbsdemo.persistence.repository;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.LoginUser;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LoginUserRepository {
    private static final String SQL_FIND_BY_USER_ID = """
            SELECT
                users.user_id,
                users.user_name,
                users.password,
                users.email,
                users.existence,
                roles.role_name
            FROM
                users
            JOIN
                user_role
            ON
                user_role.user_id = users.id
            JOIN
                roles
            ON
                roles.id = user_role.role_id
            WHERE
                users.user_id = :userId
            AND
                users.existence = true
            """;

    private static final ResultSetExtractor<LoginUser> LOGIN_USER_EXTRACTOR = (rs) -> {
        String userId = null;
        String userName = null;
        String password = null;
        String email = null;
        Boolean existence = null;
        List<String> roleList = new ArrayList<>();

        while (rs.next()) {
            if (userId == null) {
                userId = rs.getString("user_id");
                userName = rs.getString("user_name");
                password = rs.getString("password");
                email = rs.getString("email");
                existence = rs.getBoolean("existence");
            }
            roleList.add(rs.getString("role_name"));
        }
        return new LoginUser(userId, userName, password, email, existence, roleList);
    };

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LoginUserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<LoginUser> findByUserId(String userId) {
        MapSqlParameterSource param = new MapSqlParameterSource("userId", userId);
        LoginUser loginUser = namedParameterJdbcTemplate.query(SQL_FIND_BY_USER_ID, param, LOGIN_USER_EXTRACTOR);
        return Optional.ofNullable(loginUser);
    }}
