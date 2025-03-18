package com.fdn.course.monitoring.config;

import com.fdn.course.monitoring.security.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    public DataSource getDataSource(){
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(Crypto.performDecrypt(env.getProperty("spring.datasource.url")));
        dataSourceBuilder.username(Crypto.performDecrypt(env.getProperty("spring.datasource.username")));
        dataSourceBuilder.password(Crypto.performDecrypt(env.getProperty("spring.datasource.password")));
        return dataSourceBuilder.build();
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
