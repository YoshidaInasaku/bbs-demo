package com.yoshidainasaku.output.bbsdemo.persistence.entity;

import java.sql.Timestamp;

public record Content(String textContent, Timestamp updatedAt, String userId, String userName) {}