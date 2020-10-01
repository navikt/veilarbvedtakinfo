package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Date;

import static java.lang.String.format;

@Repository
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
            db.update("INSERT INTO MOTESTOTTE(DATO, AKTOR_ID) VALUES(CURRENT_TIMESTAMP,?)", aktorId.get());
        } catch (DuplicateKeyException e) {
            db.update("UPDATE MOTESTOTTE SET DATO = CURRENT_TIMESTAMP WHERE AKTOR_ID = ?", aktorId.get());
        }
    }

    public Motestotte hentMoteStotte(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %s", TABLE_NAME, AKTOR_ID, aktorId.get());
        return db.query(sql, MotestotteRepository::motestotteMapper);
    }

    @SneakyThrows
    private static Motestotte motestotteMapper(ResultSet rs) {
        if (rs.next()) {
            return new Motestotte()
                    .setDato(rs.getTimestamp(DATO));
        } else{
            return null;
        }
    }
}
