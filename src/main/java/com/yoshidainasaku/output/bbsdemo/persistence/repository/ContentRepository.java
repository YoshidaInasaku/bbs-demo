package com.yoshidainasaku.output.bbsdemo.persistence.repository;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ContentRepository {
    private static final String SQL_FIND_ALL_CONTENTS = """
            SELECT
                contents.id,
                contents.text_content,
                contents.updated_at,
                contents.user_id,
                users.user_name
            FROM
                contents
            JOIN
                users
            ON
                users.user_id = contents.user_id
            ORDER BY contents.id DESC
            LIMIT :limit
            OFFSET :offset
            """;

    private static final String SQL_COUNT_ALL_CONTENTS = """
            SELECT COUNT(contents.id)
            FROM contents
            """;

    private static final String SQL_FIND_SPECIFIC_USER_CONTENTS = """
            SELECT
                contents.text_content,
                contents.updated_at,
                contents.user_id,
                users.user_name
            FROM
                contents
            JOIN
                users
            ON
                users.user_id = contents.user_id
            WHERE
                contents.user_id = :user_id
            ORDER BY contents.id DESC
            """;

    private static final String SQL_ADD_CONTENT = """
            INSERT INTO contents(text_content, updated_at, user_id)
                VALUES(:text_content, :updated_at, :user_id)
            """;

    private static final String SQL_COUNT_SPECIFIC_USER_CONTENTS = """
            SELECT COUNT(contents.id) 
            FROM
                contents
            WHERE
                contents.user_id = :user_id
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ContentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Content> findAll(HashMap<String , String> paginationInfo) {
        int page = Integer.parseInt(paginationInfo.get("page")) - 1;
        int limit = Integer.parseInt(paginationInfo.get("limit"));

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", page * limit);

        List<Content> contentList = namedParameterJdbcTemplate.query(SQL_FIND_ALL_CONTENTS, param, new DataClassRowMapper<>(Content.class));
        return contentList;
    }

    public int countAllContents() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        Integer result = namedParameterJdbcTemplate.queryForObject(SQL_COUNT_ALL_CONTENTS, param, Integer.class);
        return result;
    }

    public void add(String textContent, String updatedAt, String userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("text_content", textContent)
                .addValue("updated_at", updatedAt)
                .addValue("user_id", userId);
        namedParameterJdbcTemplate.update(SQL_ADD_CONTENT, params);
    }

    public Integer countSpecificUserContents(String userId) {
        MapSqlParameterSource param = new MapSqlParameterSource("user_id", userId);
        Integer count = namedParameterJdbcTemplate.queryForObject(SQL_COUNT_SPECIFIC_USER_CONTENTS, param, Integer.class);
        return count;
    }

    public List<Content> findSpecificUserContents(String userId) {
        MapSqlParameterSource param = new MapSqlParameterSource("user_id", userId);
        List<Content> contentList = namedParameterJdbcTemplate.query(SQL_FIND_SPECIFIC_USER_CONTENTS, param, new DataClassRowMapper<>(Content.class));
        return contentList;
    }
}
