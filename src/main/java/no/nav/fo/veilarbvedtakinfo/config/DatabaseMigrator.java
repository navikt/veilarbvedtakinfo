package no.nav.fo.veilarbvedtakinfo.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class DatabaseMigrator {

    private final DataSource dataSource;

    @Autowired
    public DatabaseMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrateDb() {
        log.info("Starting database migration...");
        Flyway.configure().dataSource(dataSource).load().migrate();
    }
}
