package no.nav.fo.veilarbvedtakinfo.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class LocalH2Database {

    private static JdbcTemplate db;

    public static JdbcTemplate getDb() {
        if (db == null) {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:veilarbvedtakinfo-local;DB_CLOSE_DELAY=-1;MODE=Oracle;TRACE_LEVEL_SYSTEM_OUT=3;");

            db = new JdbcTemplate(dataSource);
            initDb(db);
        }

        return db;
    }

    private static void initDb(JdbcTemplate db) {
        log.info("Starting database migration...");
        Flyway.configure().dataSource(db.getDataSource()).load().migrate();
    }

}
