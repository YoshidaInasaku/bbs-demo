package com.yoshidainasaku.output.bbsdemo.persistence.repository;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentRepository {
    private final static String SQL_FIND_ALL_CONTENTS = """
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
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ContentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Content> findAll() {
        List<Content> contentList = namedParameterJdbcTemplate.query(SQL_FIND_ALL_CONTENTS, new DataClassRowMapper<>(Content.class));
        return contentList;
    }
}
