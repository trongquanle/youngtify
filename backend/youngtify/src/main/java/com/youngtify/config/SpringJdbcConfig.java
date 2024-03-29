package com.youngtify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.youngtify.config")
public class SpringJdbcConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;
    @Value("${spring.datasource.url}")
    private String urlName;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(urlName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
