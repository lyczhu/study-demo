package com.lawyus.study.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 使用主库（默认）
    public void addUser(String username) {
        jdbcTemplate.update("INSERT INTO user (username) VALUES (?)", username);
    }

    // 使用从库
    @DataSource("slave")
    public List<Map<String, Object>> listUsers() {
        return jdbcTemplate.queryForList("SELECT * FROM user");
    }
}
