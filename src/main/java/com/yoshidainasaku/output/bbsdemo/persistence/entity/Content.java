package com.yoshidainasaku.output.bbsdemo.persistence.entity;

import java.sql.Timestamp;

public record Content(String textContent, Timestamp updated_at, String userId, String userName) {}
