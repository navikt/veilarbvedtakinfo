package no.nav.veilarbvedtakinfo.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DbTestUtils {

    private final static List<String> ALL_TABLES = Arrays.asList(
            "BEHOVSVURDERING_SPORSMAL_SVAR",
            "BEHOVSVURDERING_BESVARELSE",
            "MOTESTOTTE"
    );

    public static void cleanupTestDb() {
        ALL_TABLES.forEach((table) -> deleteAllFromTable(LocalH2Database.getDb(), table));
    }

    public static JdbcTemplate getTestDb() {
        return new JdbcTemplate(dataSource());
    }

    public static DataSource dataSource() {
        return LocalH2Database.getDb().getDataSource();
    }

    private static void deleteAllFromTable(JdbcTemplate db, String tableName) {
        db.execute("DELETE FROM " + tableName);
    }
}
