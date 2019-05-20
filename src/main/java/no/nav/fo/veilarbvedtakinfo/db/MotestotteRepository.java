package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.sbl.sql.SqlUtils;
import no.nav.sbl.sql.where.WhereClause;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.Date;

public class MotestotteRepository {

    private JdbcTemplate db;
    private final static String TABLE_NAME = "MOTESTOTTE";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String DATO = "DATO";

    public MotestotteRepository(JdbcTemplate db) {
        this.db = db;
    }

    public void oppdaterMotestotte(AktorId aktorId) {
        try {
            SqlUtils.insert(db, TABLE_NAME)
                    .value(DATO, new Date())
                    .value(AKTOR_ID, aktorId.getAktorId())
                    .execute();
        } catch (DuplicateKeyException e) {
            SqlUtils.update(db, TABLE_NAME)
                    .set(DATO, new Date())
                    .whereEquals(AKTOR_ID, aktorId.getAktorId())
                    .execute();
        }
    }


    public Motestotte hentMoteStotte(AktorId aktorId) {
        return SqlUtils.select(db, TABLE_NAME, MotestotteRepository::motestotteMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .column("*")
                .execute();
    }


    @SneakyThrows
    private static Motestotte motestotteMapper(ResultSet rs) {
        return new Motestotte()
                .setDato(rs.getTimestamp(DATO));
    }
}
