package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.sbl.jdbc.DataSourceFactory;
import no.nav.sbl.sql.SqlUtils;
import no.nav.sbl.sql.where.WhereClause;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static no.nav.fo.veilarbvedtakinfo.config.DatabaseConfig.*;
import static no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository.AKTOR_ID;
import static no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository.FREMTIDIG_SITUASJON;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class DbTestUtils {

    public static JdbcTemplate getTestDb() {
        return new JdbcTemplate(getTestDataSource());
    }

    public static void cleanUp(JdbcTemplate db, String aktorId) {
        SqlUtils.delete(db, FREMTIDIG_SITUASJON).where(WhereClause.equals(AKTOR_ID, aktorId)).execute();
    }

    public static void setupInMemoryContext() {
        setInMemoryDataSourceProperties();
    }

    private static DataSource getTestDataSource() {
        return DataSourceFactory.dataSource()
                .url(getRequiredProperty(VEILARBVEDTAKINFODB_URL))
                .username(getRequiredProperty(VEILARBVEDTAKINFODB_USERNAME))
                .password(getRequiredProperty(VEILARBVEDTAKINFODB_PASSWORD))
                .build();
    }

    private static void setInMemoryDataSourceProperties() {
        System.setProperty(VEILARBVEDTAKINFODB_URL, "jdbc:h2:mem:veilarbvedtakinfo;DB_CLOSE_DELAY=-1;MODE=Oracle");
        System.setProperty(VEILARBVEDTAKINFODB_USERNAME, "sa");
        System.setProperty(VEILARBVEDTAKINFODB_PASSWORD, "password");
    }
}
