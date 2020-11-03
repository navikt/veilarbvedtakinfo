package no.nav.veilarbvedtakinfo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DatabaseMigrator {

    private final DataSource dataSource;

    @PostConstruct
    public void migrateDb() {
        log.info("Starting database migration...");
        Flyway.configure()
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
