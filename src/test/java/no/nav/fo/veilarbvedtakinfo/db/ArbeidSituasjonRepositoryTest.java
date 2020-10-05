package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertEquals;

public class ArbeidSituasjonRepositoryTest {

    private static JdbcTemplate db;
    private static ArbeidSituasjonRepository arbeidSituasjonRepository;
    private static AktorId eksternIdent = AktorId.of("123");

    @BeforeEach
    public void setup() {
        db = DbTestUtils.getTestDb();
        arbeidSituasjonRepository = new ArbeidSituasjonRepository(db);
    }

    @AfterEach
    public void tearDown() {
        DbTestUtils.cleanupTestDb();
    }

    @Test
    public void lagreSituasjon() {
        AktorId aktorId = eksternIdent;
        EndretAvType endretAv = EndretAvType.BRUKER;
        String avsenderID = "234";
        ArbeidSituasjonSvar svar = new ArbeidSituasjonSvar();
        svar.setSvarId("1");
        svar.setSvarTekst("Test");

        arbeidSituasjonRepository.lagreSituasjon(aktorId, endretAv, avsenderID, svar);

        ArbeidSituasjon arbeidSituasjon = arbeidSituasjonRepository.hentSituasjon(aktorId);

        assertEquals(endretAv.toString(), arbeidSituasjon.getEndretAvType());
        assertEquals(svar.getSvarId(), arbeidSituasjon.getSvarId());
        assertEquals(svar.getSvarTekst(), arbeidSituasjon.getSvarTekst());
        assertEquals(avsenderID, arbeidSituasjon.getEndretAvId());
    }
}
