package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;

import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HinderSvar;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Repository
public class InfoOmMegRepository {
    private JdbcTemplate db;
    public final static String FREMTIDIG_SITUASJON = "FREMTIDIG_SITUASJON";
    public final static String FREMTIDIG_SITUASJON_SEQ = "FREMTIDIG_SITUASJON_SEQ";

    public final static String AKTOR_ID = "AKTOR_ID";
    public final static String ALTERNATIV_ID = "ALTERNATIV_ID";
    public final static String FREMTIDIG_SITUASJON_ID = "FREMTIDIG_SITUASJON_ID";

    public final static String TEKST = "TEKST";
    public final static String ENDRET_AV = "ENDRET_AV";
    public final static String DATO = "DATO";

    public final static String HELSEHINDER = "HELSEHINDER";
    public final static String HELSEHINDER_SEQ = "HELSEHINDER_SEQ";
    public final static String ANDREHINDER = "ANDREHINDER";
    public final static String ANDREHINDER_SEQ = "ANDREHINDER_SEQ";
    public final static String ANDREHINDER_ID = "ANDREHINDER_ID";
    public final static String HELSEHINDER_ID = "HELSEHINDER_ID";
    public final static String SVAR = "SVAR";

    public InfoOmMegRepository(JdbcTemplate db) {
        this.db = db;
    }

    public HovedmalData hentFremtidigSituasjonForAktorId(String aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %s ORDER BY DATO DESC",
                    FREMTIDIG_SITUASJON, AKTOR_ID, aktorId);

        List<HovedmalData> data = db.query(sql, (rs, rownum) -> fremtidigSituasjonMapper(rs));
        return data.isEmpty() ? null : data.get(0);
    }

    public HovedmalData hentFremtidigSituasjonForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = %s",
                    FREMTIDIG_SITUASJON, FREMTIDIG_SITUASJON_ID, id);
        List<HovedmalData> data = db.query(sql, (rs, rownum) -> fremtidigSituasjonMapper(rs));
        return data.isEmpty() ? null : data.get(0);
    }

    public List<HovedmalData> hentSituasjonHistorikk(String aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %s ORDER BY DATO DESC",
                    FREMTIDIG_SITUASJON, AKTOR_ID, aktorId);

        return db.query(sql, (rs, rownum) -> fremtidigSituasjonMapper(rs));
    }

    public long lagreFremtidigSituasjonForAktorId(HovedmalData fremtidigSituasjonData, String aktorId, String endretAv) {
        long id = DatabaseUtils.nesteFraSekvens(db, FREMTIDIG_SITUASJON_SEQ);
        String alt = fremtidigSituasjonData.getAlternativId().toString();
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                FREMTIDIG_SITUASJON, FREMTIDIG_SITUASJON_ID, AKTOR_ID, ALTERNATIV_ID, TEKST, ENDRET_AV, DATO
        );
        db.update(sql, id, aktorId, alt, fremtidigSituasjonData.getTekst(), endretAv, new Timestamp(System.currentTimeMillis()));
        return id;
    }

    @SneakyThrows
    private static HovedmalData fremtidigSituasjonMapper(ResultSet rs) {
         return new HovedmalData()
                    .setAlternativId(ofNullable(rs.getString(ALTERNATIV_ID)).isPresent()
                            ? HovedmalSvar.valueOf(rs.getString(ALTERNATIV_ID))
                            : null
                    )
                    .setTekst(rs.getString(TEKST))
                    .setDato(rs.getTimestamp(DATO))
                    .setEndretAv(rs.getString(ENDRET_AV));
    }

    public HelseOgAndreHensynData hentHelseHinderForAktorId(String aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %s ORDER BY DATO DESC LIMIT 1",
                HELSEHINDER, AKTOR_ID, aktorId);
        return db.query(sql, InfoOmMegRepository::helseHensynMapper);
    }

    public HelseOgAndreHensynData hentHelseHinderForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = %s",
                HELSEHINDER, HELSEHINDER_ID, id);
        return db.query(sql, InfoOmMegRepository::helseHensynMapper);
    }

    public HelseOgAndreHensynData hentAndreHinderForAktorId(String aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = %s ORDER BY DATO DESC LIMIT 1",
                ANDREHINDER, AKTOR_ID, aktorId);
        return db.query(sql, InfoOmMegRepository::helseHensynMapper);
    }

    public HelseOgAndreHensynData hentAndreHinderForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = %s",
                ANDREHINDER, ANDREHINDER_ID, id);
        return db.query(sql, InfoOmMegRepository::helseHensynMapper);
    }

    public long lagreHelseHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, String aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, HELSEHINDER_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?)",
                HELSEHINDER, HELSEHINDER_ID, AKTOR_ID, SVAR, DATO
        );
        db.update(sql, id, aktorId, helseOgAndreHensynData.getVerdi().toString(), new Timestamp(System.currentTimeMillis()));
        return id;
    }

    public long lagreAndreHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, String aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, ANDREHINDER_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?)",
                ANDREHINDER, ANDREHINDER_ID, AKTOR_ID, SVAR, DATO
        );
        db.update(sql, id, aktorId, helseOgAndreHensynData.getVerdi().toString(), new Timestamp(System.currentTimeMillis()));
        return id;
    }

    @SneakyThrows
    private static HelseOgAndreHensynData helseHensynMapper(ResultSet rs) {
        while(rs.next()) {
            return new HelseOgAndreHensynData()
                        .setVerdi(ofNullable(rs.getString(SVAR)).isPresent()
                            ? HinderSvar.valueOf(rs.getString(SVAR))
                            : null
                            )
                        .setDato(rs.getTimestamp(DATO));
        }
        return null;
    }
}
