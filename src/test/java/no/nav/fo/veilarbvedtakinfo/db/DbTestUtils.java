package no.nav.fo.veilarbvedtakinfo.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Slf4j
public class DbTestUtils {

    public static JdbcTemplate getTestDb() {
        return new JdbcTemplate(dataSource());
    }

    public static void cleanUp(JdbcTemplate jdbcTemplate, String aktorId){
        String sql = "DELETE FROM FREMTIDIG_SITUASJON WHERE AKTOR_ID = ?";

        int rowsDeleted = jdbcTemplate.update(sql,  aktorId);

        if (rowsDeleted > 0) {
            log.info("Aktorid {} har blitt slettet fra FREMTIDIG_SITUASJON tabell", aktorId);
        }
    }

    public static DataSource dataSource() {
        return LocalH2Database.getDb().getDataSource();
    }
}
