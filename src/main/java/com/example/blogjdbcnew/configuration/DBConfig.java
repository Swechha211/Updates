package com.example.blogjdbcnew.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;


    @Value("${spring.datasource.username}")
    private String dbUsername;


    @Value("${spring.datasource.password}")
    private String dbPassword;


    @Value("${spring.datasource.maximumPoolSize}")
    private int maximumPoolSize;
    @Value("${spring.datasource.minimumIdle}")
    private int minimumIdle;


    @Value("${spring.datasource.connectionTimeout}")
    private int connectionTimeout;


    @Value("${spring.datasource.idleTimeout}")
    private int idleTimeout;


    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setIdleTimeout(idleTimeout);
        return dataSource;
    }
}