package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.FremtidigSituasjonData;
import no.nav.fo.veilarbvedtakinfo.domain.FremtidigSituasjonSvar;
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
    private final static String ID = "FREMTIDIG_SITUASJON_ID";

    private final static String TEKST = "TEKST";
    private final static String ENDRET_AV = "ENDRET_AV";
    private final static String DATO = "DATO";

    public InfoOmMegRepository(JdbcTemplate db) {
        this.db = db;
    }

    public FremtidigSituasjonData hentFremtidigSituasjonForAktorId(AktorId aktorId) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .limit(1)
                .column("*")
                .execute();
    }

    public FremtidigSituasjonData hentFremtidigSituasjonForId(long id) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(ID, id))
                .column("*")
                .execute();
    }

    public List<FremtidigSituasjonData> hentSituasjonHistorikk(AktorId aktorId) {
        return SqlUtils.select(db, FREMTIDIG_SITUASJON, InfoOmMegRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(DATO))
                .column("*")
                .executeToList();
    }

    public long lagreFremtidigSituasjonForAktorId(FremtidigSituasjonData fremtidigSituasjonData, AktorId aktorId, String endretAv) {
        long id = DatabaseUtils.nesteFraSekvens(db, FREMTIDIG_SITUASJON_SEQ);
        String alt = fremtidigSituasjonData.getAlternativId().toString();
        SqlUtils.insert(db, FREMTIDIG_SITUASJON)
                .value(ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(ALTERNATIV_ID, alt)
                .value(TEKST, fremtidigSituasjonData.getTekst())
                .value(ENDRET_AV, endretAv)
                .value(DATO, DbConstants.CURRENT_TIMESTAMP)
                .execute();

        return id;
    }

    @SneakyThrows
    private static FremtidigSituasjonData fremtidigSituasjonMapper(ResultSet rs) {
        return new FremtidigSituasjonData()
                .setAlternativId(ofNullable(rs.getString(ALTERNATIV_ID)).isPresent()
                        ? FremtidigSituasjonSvar.valueOf(rs.getString(ALTERNATIV_ID))
                        : null
                )
                .setTekst(rs.getString(TEKST))
                .setDato(rs.getDate(DATO))
                .setEndretAv(rs.getString(ENDRET_AV));

    }
}
