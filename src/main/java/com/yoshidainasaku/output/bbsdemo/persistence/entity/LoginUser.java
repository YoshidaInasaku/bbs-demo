package com.yoshidainasaku.output.bbsdemo.persistence.entity;

import java.util.List;

public record LoginUser
        (String userId, String userName, String password, String email, Boolean existence, List<String> roleList) {}
