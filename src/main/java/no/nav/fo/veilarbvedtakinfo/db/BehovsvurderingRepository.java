package no.nav.fo.veilarbvedtakinfo.db;

import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.fo.veilarbvedtakinfo.utils.DatabaseUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Repository
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
        String sql = format(
                "INSERT INTO %s (%s, %s, %s) VALUES (?,?,?)",
                BESVARLSE_TABLE_NAME, BESVARELSE_ID, AKTOR_ID, SIST_OPPDATERT
        );
        db.update(sql, id, aktorId.get(), new Date());
        return id;
    }

    public void leggTilNyttSvarPaBesvarelsen(long besvarlseId, Svar svar) {
        Date sistOppdatertDato = new Date();
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?)",
                SPM_SVAR_TABLE_NAME, BESVARELSE_ID, SPM_ID, SVAR, SPM, DATO
        );
        db.update(sql, besvarlseId, svar.spmId, svar.svar, svar.spm, sistOppdatertDato);

        String sql1 = "UPDATE " + BESVARLSE_TABLE_NAME +
                      " SET " + BESVARELSE_ID + " = ?, " +
                      SIST_OPPDATERT + " = ? ";
        db.update(sql1, besvarlseId, sistOppdatertDato);
    }

    public Besvarelse hentBesvarelse(Long besvarlseId) {
        String sql = format("SELECT * FROM %s WHERE %s = %d", BESVARLSE_TABLE_NAME, BESVARELSE_ID, besvarlseId);
        Besvarelse bv =  db.query(sql, BehovsvurderingRepository::besvarelseMapper);

        String sql1 = format("SELECT * FROM %s WHERE %s = %d", SPM_SVAR_TABLE_NAME, BESVARELSE_ID, besvarlseId);
        List<Svar> svar = List.of(Objects.requireNonNull(db.query(sql1, BehovsvurderingRepository::svarMapper)));

        assert bv != null;
        bv.setSvar(svar);
        return bv;
    }

    public Besvarelse hentSisteBesvarelse(AktorId aktorId) {
        String sql = format("SELECT * FROM(SELECT * FROM %s WHERE %s = %s ORDER BY SIST_OPPDATERT DESC) WHERE ROWNUM <=1",
                            BESVARLSE_TABLE_NAME, AKTOR_ID, aktorId.get());

        Besvarelse bv =  db.query(sql, BehovsvurderingRepository::besvarelseMapper);
        if (bv == null) {
            return null;
        }

        String sql1 = format("SELECT * FROM %s WHERE %s = %d", SPM_SVAR_TABLE_NAME, BESVARELSE_ID, bv.besvarelseId);
        List<Svar> svar = List.of(Objects.requireNonNull(db.query(sql1, BehovsvurderingRepository::svarMapper)));
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
