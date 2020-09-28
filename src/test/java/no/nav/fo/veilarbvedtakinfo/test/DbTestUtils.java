package no.nav.fo.veilarbvedtakinfo.test;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.types.identer.AktorId;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
public class DbTestUtils {

    public static JdbcTemplate getTestDb() {
        return new JdbcTemplate(dataSource());
    }

    public static void cleanUp(JdbcTemplate jdbcTemplate, AktorId aktorId){
        String sql = "DELETE FROM FREMTIDIG_SITUASJON WHERE AKTOR_ID = ?";

        int rowsDeleted = jdbcTemplate.update(sql,  aktorId.get());

        if (rowsDeleted > 0) {
            log.info("Aktorid {} har blitt slettet fra FREMTIDIG_SITUASJON tabell", aktorId);
        }
    }

    public static DataSource dataSource() {
        return LocalH2Database.getDb().getDataSource();
    }
}
