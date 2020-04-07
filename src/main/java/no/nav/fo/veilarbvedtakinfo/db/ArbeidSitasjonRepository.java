package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.sbl.sql.DbConstants;
import no.nav.sbl.sql.SqlUtils;
import no.nav.sbl.sql.order.OrderClause;
import no.nav.sbl.sql.where.WhereClause;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;

public class ArbeidSitasjonRepository {

    private JdbcTemplate db;

    private final static String MIN_SITUASJON = "MIN_SITUASJON";
    private final static String ID = "ID";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String OPRETTET = "OPRETTET";
    private final static String ENDRET_AV = "ENDRET_AV";
    private final static String SVAR_ID = "SVAR_ID";
    private final static String SVAR_TEXT = "SVAR_TEXT";
    private final static String MIN_SITUASJON_SEQ = "MIN_SITUASJON_SEQ";

    public ArbeidSitasjonRepository(JdbcTemplate db) {
        this.db = db;
    }


    public long lagreSitasjon(AktorId aktorId, EndretAvType endretAv, ArbeidSituasjonSvar svar) {
        long id = DatabaseUtils.nesteFraSekvens(db, MIN_SITUASJON_SEQ);

        SqlUtils.insert(db, MIN_SITUASJON)
                .value(ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(OPRETTET, DbConstants.CURRENT_TIMESTAMP)
                .value(ENDRET_AV, endretAv.toString())
                .value(SVAR_ID, svar.svarId)
                .value(SVAR_TEXT, svar.svarText)
                .execute();

        return id;
    }

    public ArbeidSituasjon hentSituasjon(AktorId aktorId) {
        return SqlUtils.select(db, MIN_SITUASJON, ArbeidSitasjonRepository::fremtidigSituasjonMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(OPRETTET))
                .column("*")
                .limit(1)
                .execute();
    }

    @SneakyThrows
    private static ArbeidSituasjon fremtidigSituasjonMapper(ResultSet rs) {
        return new ArbeidSituasjon()
                .setOprettet(rs.getString(OPRETTET))
                .setOprettet(rs.getString(ENDRET_AV))
                .setOprettet(rs.getString(SVAR_ID))
                .setOprettet(rs.getString(SVAR_TEXT));
    }

}
