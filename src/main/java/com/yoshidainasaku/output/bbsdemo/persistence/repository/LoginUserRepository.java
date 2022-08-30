package com.yoshidainasaku.output.bbsdemo.persistence.repository;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LoginUserRepository {
    private static final String SQL_FIND_BY_USER_ID = """
            SELECT
                users.user_id,
                users.user_name,
                users.password,
                users.email,
                roles.role_name
            FROM
                users
            JOIN
                user_role
            ON
                user_role.user_id = users.user_id
            JOIN
                roles
            ON
                roles.id = user_role.role_id
            WHERE
                users.user_id = :userId
            """;

    private static final ResultSetExtractor<LoginUser> LOGIN_USER_EXTRACTOR = (rs) -> {
        String userId = null;
        String userName = null;
        String password = null;
        String email = null;
        List<String> roleList = new ArrayList<>();

        while (rs.next()) {
            if (userId == null) {
                userId = rs.getString("user_id");
                userName = rs.getString("user_name");
                password = rs.getString("password");
                email = rs.getString("email");
            }
            roleList.add(rs.getString("role_name"));
        }
        return new LoginUser(userId, userName, password, email, roleList);
    };

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginUserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               PasswordEncoder passwordEncoder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<LoginUser> findByUserId(String userId) {
        MapSqlParameterSource param = new MapSqlParameterSource("userId", userId);
        LoginUser loginUser = namedParameterJdbcTemplate.query(SQL_FIND_BY_USER_ID, param, LOGIN_USER_EXTRACTOR);
        return Optional.ofNullable(loginUser);
    }

    private static final String SQL_ADD_USER = """
            INSERT INTO users(user_id, user_name, password, email)
                VALUES(:userId, :userName, :password, :email)
            """;

    private static final String SQL_ADD_USER_ROLE = """
            INSERT INTO user_role(user_id, role_id)
                VALUES(:userId, 1)
            """;

    @Transactional
    public void registerUser(String userId, String userName, String password, String email) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("userName", userName)
                .addValue("password", passwordEncoder.encode(password))
                .addValue("email", email);
        namedParameterJdbcTemplate.update(SQL_ADD_USER, params);
    }

    @Transactional
    public void registerUserRole(String userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
        namedParameterJdbcTemplate.update(SQL_ADD_USER_ROLE, params);
    }

    private static final String SQL_EXIST_USER = """
            SELECT COUNT(*) FROM users 
                WHERE user_id = :userId
            """;

    public boolean isExist(String userId) {
        MapSqlParameterSource param = new MapSqlParameterSource("userId", userId);
        Integer count = namedParameterJdbcTemplate.queryForObject(SQL_EXIST_USER, param, Integer.class);
        return count > 0;
    }
}
