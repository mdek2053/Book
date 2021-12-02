package nl.tudelft.sem11b.reservation.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@Component
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    @ConfigurationProperties("spring.datasource")
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql:sem_reservation")
//                .username("")
//                .password("")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
