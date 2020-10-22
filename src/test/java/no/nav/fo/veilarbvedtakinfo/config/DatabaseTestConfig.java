package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.test.LocalH2Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseTestConfig {

    @Bean
    public JdbcTemplate getDatabase() {
        return LocalH2Database.getDb();
    }

    @Bean
    public DataSource dataSource(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.getDataSource();
    }

}
