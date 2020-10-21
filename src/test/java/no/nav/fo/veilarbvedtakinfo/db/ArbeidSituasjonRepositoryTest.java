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
    public void lagrerSituasjonerOgHenterSiste() {
        lagreSituasjonen(EndretAvType.BRUKER, "234", "1", "SvarTekst1");
        ArbeidSituasjon endring2 = lagreSituasjonen(EndretAvType.BRUKER, "123", "2", "SvarTekst2");

        ArbeidSituasjon arbeidSituasjon = arbeidSituasjonRepository.hentSituasjon(eksternIdent);

        assertEquals(endring2.getEndretAvType(), arbeidSituasjon.getEndretAvType());
        assertEquals(endring2.getSvarId(), arbeidSituasjon.getSvarId());
        assertEquals(endring2.getSvarTekst(), arbeidSituasjon.getSvarTekst());
        assertEquals(endring2.getEndretAvId(), arbeidSituasjon.getEndretAvId());
    }

    private ArbeidSituasjon lagreSituasjonen(EndretAvType endretAvType, String avsenderId, String svarId, String svarTekst) {
        AktorId aktorId1 = eksternIdent;
        ArbeidSituasjonSvar svar = new ArbeidSituasjonSvar();
        svar.setSvarId(svarId);
        svar.setSvarTekst(svarTekst);

        arbeidSituasjonRepository.lagreSituasjon(aktorId1, endretAvType, avsenderId, svar);

        return  new ArbeidSituasjon()
                        .setEndretAvType(endretAvType.toString())
                        .setSvarId(svarId)
                        .setSvarTekst(svarTekst)
                        .setEndretAvId(avsenderId);
    }
}
