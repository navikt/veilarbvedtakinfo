package no.nav.fo.veilarbvedtakinfo.utils;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseUtils {

    public static void initDB(JdbcTemplate jdbcTemplate) {
        log.info("Starting database migration...");
        Flyway.configure().dataSource(jdbcTemplate.getDataSource()).load().migrate();
    }

    public static long nesteFraSekvens(JdbcTemplate jdbcTemplate, String sekvensNavn) {
        String sql = "Select " + sekvensNavn + ".nextval from dual";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
