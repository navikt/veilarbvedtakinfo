package no.nav.veilarbvedtakinfo.db;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import no.nav.common.types.identer.AktorId;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.veilarbvedtakinfo.utils.DatabaseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Repository
public class BehovsvurderingRepository {

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
    private final static int ROWNUM = 1;

    private final JdbcTemplate db;

    public long lagNyBesvarlse(AktorId aktorId) {
        long id = DatabaseUtils.nesteFraSekvens(db, SEQ);
        String sql = format(
                "INSERT INTO %s (%s, %s, %s) VALUES (?,?,CURRENT_TIMESTAMP)",
                BESVARLSE_TABLE_NAME, BESVARELSE_ID, AKTOR_ID, SIST_OPPDATERT
        );
        db.update(sql, id, aktorId.get());
        return id;
    }

    public void leggTilNyttSvarPaBesvarelsen(long besvarlseId, Svar svar) {
        String sql = format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?, CURRENT_TIMESTAMP)",
                SPM_SVAR_TABLE_NAME, BESVARELSE_ID, SPM_ID, SVAR, SPM, DATO
        );
        db.update(sql, besvarlseId, svar.spmId, svar.svar, svar.spm);

        String sql1 = format("UPDATE %s SET %s = CURRENT_TIMESTAMP WHERE %s = ?", BESVARLSE_TABLE_NAME, SIST_OPPDATERT, BESVARELSE_ID);

        db.update(sql1, besvarlseId);
    }

    public Besvarelse hentBesvarelse(Long besvarlseId) {
        String sql = format("SELECT * FROM %s WHERE %s = ?", BESVARLSE_TABLE_NAME, BESVARELSE_ID);
        List<Besvarelse> besvarelseList = db.query(sql, besvarelseMapper(), besvarlseId);

        if (besvarelseList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mangler besvarelse i behovsvurdering");
        }
        Besvarelse besvarelse = besvarelseList.get(0);
        besvarelse.setSvar(hentSvarPaBesvarelse(besvarelse.getBesvarelseId()));
        return besvarelse;
    }

    public Besvarelse hentSisteBesvarelse(AktorId aktorId) {
        String sql = format("SELECT * FROM %s WHERE %s = ? ORDER BY SIST_OPPDATERT DESC FETCH NEXT %d ROWS ONLY",
                            BESVARLSE_TABLE_NAME, AKTOR_ID, ROWNUM);
        List<Besvarelse> besvarelseList = db.query(sql, besvarelseMapper(), aktorId.get());

        if (besvarelseList.isEmpty()) {
            return null;
        }
        Besvarelse besvarelse = besvarelseList.get(0);
        besvarelse.setSvar(hentSvarPaBesvarelse(besvarelse.getBesvarelseId()));
        return besvarelse;
    }

    public List<Svar> hentSvarPaBesvarelse(Long besvarelseId) {
        String sql = format("SELECT * FROM %s WHERE %s = %d", SPM_SVAR_TABLE_NAME, BESVARELSE_ID, besvarelseId);
        return db.query(sql, svarMapper());
    }

    @SneakyThrows
    private RowMapper<Besvarelse> besvarelseMapper() {
        return (rs, rowNum) ->
                new Besvarelse()
                        .setAktorId(AktorId.of(rs.getString(AKTOR_ID)))
                        .setBesvarelseId(rs.getLong(BESVARELSE_ID))
                        .setSistOppdatert(DatabaseUtils.hentZonedDateTime(rs, SIST_OPPDATERT));
    }

    @SneakyThrows
    private RowMapper<Svar> svarMapper() {
        return (rs, rowNum) ->
                new Svar()
                        .setBesvarelseId(rs.getLong(BESVARELSE_ID))
                        .setSpmId(rs.getString(SPM_ID))
                        .setSpm(rs.getString(SPM))
                        .setSvar(rs.getString(SVAR))
                        .setDato(DatabaseUtils.hentZonedDateTime(rs, DATO));
    }
}
