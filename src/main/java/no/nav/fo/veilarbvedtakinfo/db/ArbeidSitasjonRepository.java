package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils;

import java.sql.ResultSet;

import static java.lang.String.format;

@Repository
public class ArbeidSitasjonRepository {

    private JdbcTemplate db;

    private final static String MIN_SITUASJON = "MIN_SITUASJON";
    private final static String ID = "ID";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String OPRETTET = "OPRETTET";
    private final static String ENDRET_AV_TYPE = "ENDRET_AV_TYPE";
    private final static String SVAR_ID = "SVAR_ID";
    private final static String SVAR_TEXT = "SVAR_TEXT";
    private final static String ENDRET_AV_ID = "ENDRET_AV_ID";
    private final static String MIN_SITUASJON_SEQ = "MIN_SITUASJON_SEQ";
    private final static String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";

    public ArbeidSitasjonRepository(JdbcTemplate db) {
        this.db = db;
    }


    public long lagreSitasjon(String aktorId, EndretAvType endretAv, String avsenderID, ArbeidSituasjonSvar svar) {
        long id = DatabaseUtils.nesteFraSekvens(db, MIN_SITUASJON_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                MIN_SITUASJON, ID, AKTOR_ID, OPRETTET, ENDRET_AV_ID, ENDRET_AV_TYPE, SVAR_ID, SVAR_TEXT
        );
        db.update(sql, id, aktorId, "CURRENT_TIMESTAMP", avsenderID, endretAv.toString(), svar.svarId, svar.svarTekst);
        return id;
    }

    public ArbeidSituasjon hentSituasjon(String aktorId) {
        String sql = format("SELECT * FROM(SELECT * FROM %s WHERE %s = %d ORDER BY OPRETTET DESC) WHERE ROWNUM <=1 ",
                            MIN_SITUASJON, AKTOR_ID, aktorId, OPRETTET
        );
        return db.query(sql, ArbeidSitasjonRepository::fremtidigSituasjonMapper);
    }

    @SneakyThrows
    private static ArbeidSituasjon fremtidigSituasjonMapper(ResultSet rs) {
        return new ArbeidSituasjon()
                .setOpprettet(rs.getString(OPRETTET))
                .setEndretAvType(rs.getString(ENDRET_AV_TYPE))
                .setEndretAvId(rs.getString(ENDRET_AV_ID))
                .setSvarId(rs.getString(SVAR_ID))
                .setSvarTekst(rs.getString(SVAR_TEXT));
    }

}
