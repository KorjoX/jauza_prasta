package com.mycompany.jauza;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class AppConfig {

    // 1. Mengatur Koneksi Database (DataSource)
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        // Menentukan driver MySQL versi terbaru
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Alamat database db_jauza di Laragon
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_jauza");
        
        // Kredensial default Laragon (sesuaikan jika Anda memakai password)
        dataSource.setUsername("root");
        dataSource.setPassword(""); 
        
        return dataSource;
    }

    // 2. Mengaktifkan Alat Bantu Eksekusi SQL dari Spring (JdbcTemplate)
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}