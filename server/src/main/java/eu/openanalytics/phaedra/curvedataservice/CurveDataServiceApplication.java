/**
 * Phaedra II
 *
 * Copyright (C) 2016-2022 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.curvedataservice;

import eu.openanalytics.phaedra.util.auth.AuthenticationConfigHelper;
import eu.openanalytics.phaedra.util.auth.AuthorizationServiceFactory;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import eu.openanalytics.phaedra.util.jdbc.JDBCUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.time.Clock;

@EnableWebSecurity
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableKafka
public class CurveDataServiceApplication {

    private final Environment environment;
    private final ServletContext servletContext;

    public CurveDataServiceApplication(Environment environment, ServletContext servletContext) {
        this.environment = environment;
        this.servletContext = servletContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(CurveDataServiceApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        String url = environment.getProperty("DB_URL");
        String username = environment.getProperty("DB_USER");
        String password = environment.getProperty("DB_PASSWORD");

        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("No database URL configured: " + url);
        }
        String driverClassName = JDBCUtils.getDriverClassName(url);
        if (driverClassName == null) {
            throw new RuntimeException("Unsupported database type: " + url);
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server().url(environment.getProperty("API_URL")).description("Default Server URL");
        return new OpenAPI().addServersItem(server);
    }

    @Bean
    public IAuthorizationService authService() {
        return AuthorizationServiceFactory.create();
    }

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        return AuthenticationConfigHelper.configure(http);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
