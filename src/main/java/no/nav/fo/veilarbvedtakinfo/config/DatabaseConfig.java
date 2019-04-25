package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.sbl.jdbc.DataSourceFactory;
import no.nav.sbl.jdbc.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    public static final String VEILARBVEDTAKINFODB_URL = "VEILARBVEDTAKINFODB_URL";
    public static final String VEILARBVEDTAKINFODB_USERNAME = "VEILARBVEDTAKINFODB_USERNAME";
    public static final String VEILARBVEDTAKINFODB_PASSWORD = "VEILARBVEDTAKINFODB_PASSWORD";

    @Bean
    public static DataSource getDataSource() {
        return DataSourceFactory.dataSource()
                .url(getRequiredProperty(VEILARBVEDTAKINFODB_URL))
                .username(getRequiredProperty(VEILARBVEDTAKINFODB_USERNAME))
                .password(getOptionalProperty(VEILARBVEDTAKINFODB_PASSWORD).orElse(""))
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public Database database(JdbcTemplate jdbcTemplate) {
        return new Database(jdbcTemplate);
    }

}
