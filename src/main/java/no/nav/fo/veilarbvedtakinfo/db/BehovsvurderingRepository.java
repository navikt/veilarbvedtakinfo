package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.sbl.sql.SqlUtils;
import no.nav.sbl.sql.order.OrderClause;
import no.nav.sbl.sql.where.WhereClause;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class BehovsvurderingRepository {

    private JdbcTemplate db;
    private final static String SEQ = "BEHOVSVURDERING_SEQ";
    private final static String BESVARLSE_TABLE_NAME = "BEHOVSVURDERING_BESVARELSE";
    private final static String SPM_SVAR_TABLE_NAME = "BEHOVSVURDERING_SPORSMAL_SVAR";

    private final static String BESVARELSE_ID = "BESVARELSE_ID";
    private final static String AKTOR_ID = "AKTOR_ID";
    private final static String SIST_OPPDATERT = "SIST_OPPDATERT";

    private final static String SPM_ID = "SPM_ID";
    private final static String SPM = "SPM";
    private final static String SVAR = "SVAR";
    private final static String DATO = "DATO";

    public BehovsvurderingRepository(JdbcTemplate db) {
        this.db = db;
    }

    public long lagNyBesvarlse(AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, SEQ);
        SqlUtils.insert(db, BESVARLSE_TABLE_NAME)
                .value(BESVARELSE_ID, id)
                .value(AKTOR_ID, aktorId.getAktorId())
                .value(SIST_OPPDATERT, new Date())
                .execute();

        return id;
    }

    public void leggTilNyttSvarPaBesvarelsen(long besvarlseId, Svar svar) {
        Date sistOppdatertDato = new Date();
        SqlUtils.insert(db, SPM_SVAR_TABLE_NAME)
                .value(BESVARELSE_ID, besvarlseId)
                .value(SPM_ID, svar.spmId)
                .value(SVAR, svar.svar)
                .value(SPM, svar.spm)
                .value(DATO, sistOppdatertDato)
                .execute();

        SqlUtils.update(db, BESVARLSE_TABLE_NAME)
                .whereEquals(BESVARELSE_ID, besvarlseId)
                .set(SIST_OPPDATERT, sistOppdatertDato)
                .execute();

    }

    public Besvarelse hentBesvarelse(Long besvarlseId) {

        Besvarelse bv = SqlUtils.select(db, BESVARLSE_TABLE_NAME, BehovsvurderingRepository::besvarelseMapper)
                .where(WhereClause.equals(BESVARELSE_ID, besvarlseId))
                .column("*")
                .execute();

        List<Svar> svar = SqlUtils.select(db, SPM_SVAR_TABLE_NAME, BehovsvurderingRepository::svarMapper)
                .where(WhereClause.equals(BESVARELSE_ID, besvarlseId))
                .column("*")
                .executeToList();
        bv.setSvar(svar);
        return bv;
    }


    public Besvarelse hentSisteBesvarelse(AktorId aktorId) {

        Besvarelse bv = SqlUtils.select(db, BESVARLSE_TABLE_NAME, BehovsvurderingRepository::besvarelseMapper)
                .where(WhereClause.equals(AKTOR_ID, aktorId.getAktorId()))
                .orderBy(OrderClause.desc(SIST_OPPDATERT))
                .limit(1)
                .column("*")
                .execute();

        if (bv == null) {
            return null;
        }

        List<Svar> svar = SqlUtils.select(db, SPM_SVAR_TABLE_NAME, BehovsvurderingRepository::svarMapper)
                .where(WhereClause.equals(BESVARELSE_ID, bv.besvarelseId))
                .column("*")
                .executeToList();

        bv.setSvar(svar);
        return bv;
    }

    @SneakyThrows
    private static Besvarelse besvarelseMapper(ResultSet rs) {
        return new Besvarelse()
                .setBesvarelseId(rs.getLong(BESVARELSE_ID))
                .setSistOppdatert(rs.getTimestamp(SIST_OPPDATERT));
    }

    @SneakyThrows
    private static Svar svarMapper(ResultSet rs) {
        return new Svar()
                .setBesvarelseId(rs.getLong(BESVARELSE_ID))
                .setSpmId(rs.getString(SPM_ID))
                .setSpm(rs.getString(SPM))
                .setSvar(rs.getString(SVAR))
                .setDato(rs.getTimestamp(DATO));
    }
}
