package com.mycompany.jauza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAOImpl implements AdminDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean login(String username, String password) {
        String sql = "SELECT count(*) FROM admin WHERE username=? AND password=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count != null && count > 0;
    }
}