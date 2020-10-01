package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.fo.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertNotNull;

public class MotestotteRepositoryTest {

    private static JdbcTemplate db;
    private static MotestotteRepository motestotteRepository;
    private static AktorId eksternIdent = AktorId.of("123");

    @BeforeEach
    public void setup() {
        db = DbTestUtils.getTestDb();
        motestotteRepository = new MotestotteRepository(db);
    }

    @AfterEach
    public void tearDown() {
        DbTestUtils.cleanupTestDb();
    }

    @Test
    public void oppdaterMotestotte() {
        motestotteRepository.oppdaterMotestotte(eksternIdent);

        Motestotte motestotte = motestotteRepository.hentMoteStotte(eksternIdent);

        assertNotNull(motestotte.getDato());
    }
}
