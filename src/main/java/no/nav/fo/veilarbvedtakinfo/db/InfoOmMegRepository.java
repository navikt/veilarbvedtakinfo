package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HinderSvar;
import no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils.hentZonedDateTime;

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

    public HovedmalData hentFremtidigSituasjonForAktorId(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY DATO DESC",
                    FREMTIDIG_SITUASJON, AKTOR_ID);

        List<HovedmalData> data = db.query(sql, fremtidigSituasjonMapper(), aktorId.get());
        return data.isEmpty() ? null : data.get(0);
    }

    public HovedmalData hentFremtidigSituasjonForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = ?",
                    FREMTIDIG_SITUASJON, FREMTIDIG_SITUASJON_ID);

        List<HovedmalData> data = db.query(sql, fremtidigSituasjonMapper(), id);
        return data.isEmpty() ? null : data.get(0);
    }

    public List<HovedmalData> hentSituasjonHistorikk(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY DATO DESC",
                    FREMTIDIG_SITUASJON, AKTOR_ID);

        return db.query(sql, fremtidigSituasjonMapper(), aktorId.get());
    }

    public long lagreFremtidigSituasjonForAktorId(HovedmalData fremtidigSituasjonData, AktorId aktorId, String endretAv) {
        long id = DatabaseUtils.nesteFraSekvens(db, FREMTIDIG_SITUASJON_SEQ);
        String alt = fremtidigSituasjonData.getAlternativId().toString();

        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)",
                FREMTIDIG_SITUASJON, FREMTIDIG_SITUASJON_ID, AKTOR_ID, ALTERNATIV_ID, TEKST, ENDRET_AV, DATO
        );
        db.update(sql, id, aktorId.get(), alt, fremtidigSituasjonData.getTekst(), endretAv);
        return id;
    }

    @SneakyThrows
    private static RowMapper<HovedmalData> fremtidigSituasjonMapper() {
         return (rs, rowNum) ->
                 new HovedmalData()
                    .setAlternativId(ofNullable(rs.getString(ALTERNATIV_ID)).isPresent()
                            ? HovedmalSvar.valueOf(rs.getString(ALTERNATIV_ID))
                            : null
                    )
                    .setTekst(rs.getString(TEKST))
                    .setDato(hentZonedDateTime(rs, DATO))
                    .setEndretAv(rs.getString(ENDRET_AV));
    }

    public HelseOgAndreHensynData hentHelseHinderForAktorId(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY DATO DESC FETCH NEXT 1 ROWS ONLY",
                HELSEHINDER, AKTOR_ID);
        List<HelseOgAndreHensynData> query = db.query(sql, helseHensynMapper(), aktorId.get());
        return query.isEmpty() ? null : query.get(0);
    }

    public HelseOgAndreHensynData hentHelseHinderForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = ?",
                HELSEHINDER, HELSEHINDER_ID);
        List<HelseOgAndreHensynData> query = db.query(sql, helseHensynMapper(), id);
        return query.isEmpty() ? null : query.get(0);
    }

    public HelseOgAndreHensynData hentAndreHinderForAktorId(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY DATO DESC FETCH NEXT 1 ROWS ONLY",
                ANDREHINDER, AKTOR_ID);
        List<HelseOgAndreHensynData> query = db.query(sql, helseHensynMapper(), aktorId.get());
        return query.isEmpty() ? null : query.get(0);
    }

    public HelseOgAndreHensynData hentAndreHinderForId(long id) {
        String sql = format("SELECT * FROM %s WHERE %s = ?",
                ANDREHINDER, ANDREHINDER_ID);
        List<HelseOgAndreHensynData> query = db.query(sql, helseHensynMapper(), id);
        return query.isEmpty() ? null : query.get(0);
    }
    public long lagreHelseHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, HELSEHINDER_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,CURRENT_TIMESTAMP)",
                HELSEHINDER, HELSEHINDER_ID, AKTOR_ID, SVAR, DATO
        );
        db.update(sql, id, aktorId.get(), helseOgAndreHensynData.getVerdi().toString());
        return id;
    }

    public long lagreAndreHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, ANDREHINDER_SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,CURRENT_TIMESTAMP)",
                ANDREHINDER, ANDREHINDER_ID, AKTOR_ID, SVAR, DATO
        );
        db.update(sql, id, aktorId.get(), helseOgAndreHensynData.getVerdi().toString());
        return id;
    }

    private RowMapper<HelseOgAndreHensynData> helseHensynMapper() {
        return (rs, rowNum) -> new HelseOgAndreHensynData()
                .setVerdi(ofNullable(rs.getString(SVAR)).isPresent()
                        ? HinderSvar.valueOf(rs.getString(SVAR))
                        : null
                )
                .setDato(hentZonedDateTime(rs, DATO));
    }
}
