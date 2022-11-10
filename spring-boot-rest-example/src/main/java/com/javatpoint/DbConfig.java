package com.javatpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    DataSource dataSource(
            final @Value("${spring.datasource.driverClassName}") String driverClassName,
            final @Value("${spring.datasource.url}") String url,
            final @Value("${spring.datasource.username}") String username,
            final @Value("${spring.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
