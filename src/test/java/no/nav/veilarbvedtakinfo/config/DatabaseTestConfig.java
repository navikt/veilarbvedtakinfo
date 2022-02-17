package no.nav.veilarbvedtakinfo.config;

import no.nav.veilarbvedtakinfo.test.LocalH2Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseTestConfig {

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        return LocalH2Database.getDb().getDataSource();
    }

}
