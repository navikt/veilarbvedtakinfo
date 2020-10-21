package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

import static java.lang.String.format;

@Repository
public class ArbeidSituasjonRepository {

    private JdbcTemplate db;
    private final static String MIN_SITUASJON = "MIN_SITUASJON";
    private final static String ID = "ID";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String OPPRETTET = "OPRETTET";
    private final static String ENDRET_AV_TYPE = "ENDRET_AV_TYPE";
    private final static String SVAR_ID = "SVAR_ID";
    private final static String SVAR_TEXT = "SVAR_TEXT";
    private final static String ENDRET_AV_ID = "ENDRET_AV_ID";
    private final static String MIN_SITUASJON_SEQ = "MIN_SITUASJON_SEQ";
    private final static int ROWNUM = 1;

    public ArbeidSituasjonRepository(JdbcTemplate db) {
        this.db = db;
    }

    public void lagreSituasjon(AktorId aktorId, EndretAvType endretAv, String avsenderID, ArbeidSituasjonSvar svar) {
        long id = DatabaseUtils.nesteFraSekvens(db, MIN_SITUASJON_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?,?,CURRENT_TIMESTAMP,?,?,?,?)",
                MIN_SITUASJON, ID, AKTOR_ID, OPPRETTET, ENDRET_AV_ID, ENDRET_AV_TYPE, SVAR_ID, SVAR_TEXT
        );
        db.update(sql, id, aktorId.get(), avsenderID, endretAv.toString(), svar.svarId, svar.svarTekst);
    }

    public ArbeidSituasjon hentSituasjon(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC FETCH NEXT %d ROWS ONLY",
                MIN_SITUASJON, AKTOR_ID, OPPRETTET, ROWNUM
        );
        return db.query(sql, ArbeidSituasjonRepository::fremtidigSituasjonMapper, aktorId.get());
    }

    @SneakyThrows
    private static ArbeidSituasjon fremtidigSituasjonMapper(ResultSet rs) {
        if(rs.next()) {
            return new ArbeidSituasjon()
                    .setOpprettet(rs.getString(OPPRETTET))
                    .setEndretAvType(rs.getString(ENDRET_AV_TYPE))
                    .setEndretAvId(rs.getString(ENDRET_AV_ID))
                    .setSvarId(rs.getString(SVAR_ID))
                    .setSvarTekst(rs.getString(SVAR_TEXT));
        }else {
            return null;
        }
    }

}
