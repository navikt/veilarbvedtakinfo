package no.nav.fo.veilarbvedtakinfo.db;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseUtils {
    public static void createTables(JdbcTemplate jdbcTemplate) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(jdbcTemplate.getDataSource());
        flyway.migrate();
    }

    public static long nesteFraSekvens(JdbcTemplate jdbcTemplate, String sekvensNavn) {
        return jdbcTemplate.queryForObject("select " + sekvensNavn + ".nextval from dual", Long.class);
    }

}
