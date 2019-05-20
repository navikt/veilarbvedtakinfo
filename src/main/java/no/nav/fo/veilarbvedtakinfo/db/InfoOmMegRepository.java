package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.sbl.sql.DbConstants;
import no.nav.sbl.sql.SqlUtils;
import no.nav.sbl.sql.order.OrderClause;
import no.nav.sbl.sql.where.WhereClause;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.List;

import static java.util.Optional.ofNullable;


public class InfoOmMegRepository {
    private JdbcTemplate db;
    private final static String FREMTIDIG_SITUASJON = "FREMTIDIG_SITUASJON";
    private final static String FREMTIDIG_SITUASJON_SEQ = "FREMTIDIG_SITUASJON_SEQ";

    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String ALTERNATIV_ID = "ALTERNATIV_ID";
    private final static String FREMTIDIG_SITUASJON_ID = "FREMTIDIG_SITUASJON_ID";

    private final static String TEKST = "TEKST";
    private final static String ENDRET_AV = "ENDRET_AV";
    private final static String DATO = "DATO";

    private final static String HELSEHINDER = "HELSEHINDER";
    private final static String HELSEHINDER_SEQ = "HELSEHINDER_SEQ";
    private final static String ANDREHINDER = "ANDREHINDER";
    private final static String ANDREHINDER_SEQ = "ANDREHINDER_SEQ";
    private final static String ANDREHINDER_ID = "ANDREHINDER_ID";
    private final static String HELSEHINDER_ID = "HELSEHINDER_ID";
    private final static String SVAR = "SVAR";

    public InfoOmMegRepository(JdbcTemplate db) {
        this.db = db;
    }

    public HovedmalData hentFremtidigSituasjonForAktorId(AktorId aktorId) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .limit(1)
                .column("*")
                .execute();
    }

    public HovedmalData hentFremtidigSituasjonForId(long id) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(FREMTIDIG_SITUASJON_ID, id))
                .column("*")
                .execute();
    }

    public List<HovedmalData> hentSituasjonHistorikk(AktorId aktorId) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .column("*")
                .executeToList();
    }

    public long lagreFremtidigSituasjonForAktorId(HovedmalData fremtidigSituasjonData, AktorId aktorId, String endretAv) {
        long id = DatabaseUtils.nesteFraSekvens(db, FREMTIDIG_SITUASJON_SEQ);
        String alt = fremtidigSituasjonData.getAlternativId().toString();
        SqlUtils.insert(db, FREMTIDIG_SITUASJON)
                .value(FREMTIDIG_SITUASJON_ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(ALTERNATIV_ID, alt)
                .value(TEKST, fremtidigSituasjonData.getTekst())
                .value(ENDRET_AV, endretAv)
                .value(DATO, DbConstants.CURRENT_TIMESTAMP)
                .execute();

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
                .setDato(rs.getDate(DATO))
                .setEndretAv(rs.getString(ENDRET_AV));

    }

    public HelseOgAndreHensynData hentHelseHinderForAktorId(AktorId aktorId) {
        return SqlUtils.select(db, HELSEHINDER, InfoOmMegRepository::helseHensynMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .limit(1)
                .column("*")
                .execute();
    }

    public HelseOgAndreHensynData hentHelseHinderForId(long id) {
        return SqlUtils.select(db, HELSEHINDER, InfoOmMegRepository::helseHensynMapper)
                .where(WhereClause.equals(HELSEHINDER_ID, id))
                .column("*")
                .execute();
    }

    public HelseOgAndreHensynData hentAndreHinderForAktorId(AktorId aktorId) {
        return SqlUtils.select(db, ANDREHINDER, InfoOmMegRepository::helseHensynMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .limit(1)
                .column("*")
                .execute();
    }

    public HelseOgAndreHensynData hentAndreHinderForId(long id) {
        return SqlUtils.select(db, ANDREHINDER, InfoOmMegRepository::helseHensynMapper)
                .where(WhereClause.equals(ANDREHINDER_ID, id))
                .column("*")
                .execute();
    }

    public long lagreHelseHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, HELSEHINDER_SEQ);
        SqlUtils.insert(db, HELSEHINDER)
                .value(HELSEHINDER_ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(SVAR, helseOgAndreHensynData.isVerdi())
                .value(DATO, DbConstants.CURRENT_TIMESTAMP)
                .execute();

        return id;
    }

    public long lagreAndreHinderForAktorId(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, ANDREHINDER_SEQ);
        SqlUtils.insert(db, ANDREHINDER)
                .value(ANDREHINDER_ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(SVAR, helseOgAndreHensynData.isVerdi())
                .value(DATO, DbConstants.CURRENT_TIMESTAMP)
                .execute();

        return id;
    }

    @SneakyThrows
    private static HelseOgAndreHensynData helseHensynMapper(ResultSet rs) {
        return new HelseOgAndreHensynData()
                .setVerdi(rs.getBoolean(SVAR))
                .setDato(rs.getDate(DATO));

    }

    //For tester
    public void cleanUp(String aktorId){
        SqlUtils.delete(db, FREMTIDIG_SITUASJON).where(WhereClause.equals(AKTOR_ID, aktorId)).execute();


    }
}
