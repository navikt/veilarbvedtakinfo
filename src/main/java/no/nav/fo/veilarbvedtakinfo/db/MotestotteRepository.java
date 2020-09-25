package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
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

    public void oppdaterMotestotte(String aktorId) {
        try {
            String sql = format(
                    "INSERT INTO %s (%s, %s) VALUES (?,?)",
                    TABLE_NAME, DATO, AKTOR_ID
            );
            db.update(sql, new Date(), aktorId);
        } catch (DuplicateKeyException e) {
                format(
                      "UPDATE " + TABLE_NAME +
                      " SET " + DATO + " = " + new Date() +
                      " WHERE " + AKTOR_ID + " = " + aktorId
                );
        }
    }

    public Motestotte hentMoteStotte(String aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %d",
                TABLE_NAME, AKTOR_ID, aktorId);
        return db.query(sql, MotestotteRepository::motestotteMapper);
    }

    @SneakyThrows
    private static Motestotte motestotteMapper(ResultSet rs) {
        return new Motestotte()
                .setDato(rs.getTimestamp(DATO));
    }
}
