package nl.tudelft.sem11b.reservation.repository;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    /**
     * Factory for DB connection.
     * @return DataSourceBuilder with DB connection config
     */
    @ConfigurationProperties("spring.datasource")
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql:sem_reservation")
                //.username("")
                //.password("")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
