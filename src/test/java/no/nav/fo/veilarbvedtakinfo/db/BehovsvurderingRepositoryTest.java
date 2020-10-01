package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.fo.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BehovsvurderingRepositoryTest {
    private static JdbcTemplate db;
    private static BehovsvurderingRepository behovsvurderingRepository;
    private static AktorId eksternIdent = AktorId.of("123");

    @BeforeEach
    public void setup() {
        db = DbTestUtils.getTestDb();
        behovsvurderingRepository = new BehovsvurderingRepository(db);
    }

    @AfterEach
    public void tearDown() {
        DbTestUtils.cleanupTestDb();
    }

    @Test
    public void lagNyBesvarelseTest(){
        long id = behovsvurderingRepository.lagNyBesvarlse(eksternIdent);
        Besvarelse besvarelse = behovsvurderingRepository.hentBesvarelse(id);

        assertNotNull(besvarelse.getBesvarelseId());
    }

    @Test
    public void leggTilNyttSvarPaBesvarelsenTest() {
        long id = behovsvurderingRepository.lagNyBesvarlse(eksternIdent);
        Besvarelse besvarelse = behovsvurderingRepository.hentBesvarelse(id);
        Long besvarelseId = besvarelse.getBesvarelseId();

        behovsvurderingRepository.leggTilNyttSvarPaBesvarelsen(besvarelseId, new Svar()
                                                                             .setBesvarelseId(besvarelseId)
                                                                             .setSpmId("123")
                                                                             .setSpm("spm")
                                                                             .setSvar("svar")
                                                                             .setDato(new Timestamp(System.currentTimeMillis())));

        List<Svar> svarList = behovsvurderingRepository.hentSvarPaBesvarelse(besvarelseId);
        assertNotNull(svarList);

        Svar svar = svarList.get(0);
        assertEquals(besvarelseId, svar.getBesvarelseId());
        assertEquals("123", svar.getSpmId());
        assertEquals("spm", svar.getSpm());
        assertEquals("svar", svar.getSvar());
        assertNotNull(svar.getDato());
    }
}
