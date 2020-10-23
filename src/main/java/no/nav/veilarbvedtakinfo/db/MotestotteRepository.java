package no.nav.veilarbvedtakinfo.db;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.veilarbvedtakinfo.utils.DatabaseUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Repository
public class MotestotteRepository {

    private final static String TABLE_NAME = "MOTESTOTTE";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String DATO = "DATO";

    private final JdbcTemplate db;

    public void oppdaterMotestotte(AktorId aktorId) {
        try {
            db.update("INSERT INTO MOTESTOTTE(DATO, AKTOR_ID) VALUES(CURRENT_TIMESTAMP,?)", aktorId.get());
        } catch (DuplicateKeyException e) {
            db.update("UPDATE MOTESTOTTE SET DATO = CURRENT_TIMESTAMP WHERE AKTOR_ID = ?", aktorId.get());
        }
    }

    public Motestotte hentMoteStotte(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, AKTOR_ID);
        List<Motestotte> query = db.query(sql, motestotteMapper(), aktorId.get());
        return query.isEmpty() ? null : query.get(0);
    }

    @SneakyThrows
    private RowMapper<Motestotte> motestotteMapper() {
        return (rs, rowNum) -> new Motestotte()
                .setDato(DatabaseUtils.hentZonedDateTime(rs, DATO));
    }
}
