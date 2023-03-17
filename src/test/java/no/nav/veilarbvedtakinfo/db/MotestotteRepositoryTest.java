package no.nav.veilarbvedtakinfo.db;

import no.nav.common.types.identer.AktorId;
import no.nav.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MotestotteRepositoryTest {

    private MotestotteRepository motestotteRepository;
    private final AktorId eksternIdent = AktorId.of("123");

    @BeforeEach
    public void setup() {
        JdbcTemplate db = DbTestUtils.getTestDb();
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
