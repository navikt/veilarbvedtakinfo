package no.nav.fo.veilarbvedtakinfo;

import no.nav.fo.veilarbvedtakinfo.config.ApplicationTestConfig;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseUtils;

import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonSvar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static no.nav.fo.veilarbvedtakinfo.db.DatabaseTestContext.setupInMemoryContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InfoOmMegRepositoryTest {
    private static AnnotationConfigApplicationContext context;
    private static InfoOmMegRepository infoOmMegRepository;
    private static String eksternIdent = "123";

    @BeforeEach
    public void setup() {
        setupInMemoryContext();
        TestContext.setup();
        context = new AnnotationConfigApplicationContext(
                ApplicationTestConfig.class
        );

        context.start();

        DatabaseUtils.createTables((JdbcTemplate) context.getBean("jdbcTemplate"));
        infoOmMegRepository = context.getBean(InfoOmMegRepository.class);
    }

    @AfterEach
    public void tearDown() {
        context.stop();
        infoOmMegRepository.cleanUp(eksternIdent);
    }

    @Test
    public void hentFremtidigSituasjon_empty_success() {
       FremtidigSituasjonData data = infoOmMegRepository.hentFremtidigSituasjonForAktorId(new AktorId(eksternIdent));

       assertEquals(null, data);
    }

    @Test
    public void hentSituasjonHistorikk_empty_success() {
        List<FremtidigSituasjonData> data = infoOmMegRepository.hentSituasjonHistorikk(new AktorId(eksternIdent));

        assertEquals(0, data.size());
    }

    @Test
    public void lagreFremtidigSituasjonForAktorId_success() {
        AktorId aktorId = new AktorId(eksternIdent);
        FremtidigSituasjonData data = new FremtidigSituasjonData()
            .setAlternativId(FremtidigSituasjonSvar.SAMME_ARBEIDSGIVER)
            .setTekst("Test");

        long id = infoOmMegRepository.lagreFremtidigSituasjonForAktorId(data, aktorId, eksternIdent);
        FremtidigSituasjonData actual = infoOmMegRepository.hentFremtidigSituasjonForId(id);

        assertEquals(data.getAlternativId(), actual.getAlternativId());
        assertEquals(data.getTekst(), actual.getTekst());
        assertEquals("BRUKER", actual.getEndretAv());
        assertNotEquals(null, actual.getDato());
    }

    @Test
    public void hentSituasjonHistorikk_non_empty_success() throws InterruptedException {
        FremtidigSituasjonData svar1 = lagreFremtidigSituasjon(FremtidigSituasjonSvar.NY_ARBEIDSGIVER, "Svar1", eksternIdent, eksternIdent);

        Thread.sleep(5); //For å unngå identisk dato

        FremtidigSituasjonData svar2 = lagreFremtidigSituasjon(FremtidigSituasjonSvar.SAMME_ARBEIDSGIVER_NY_STILLING, "Svar2", eksternIdent, eksternIdent);

        List<FremtidigSituasjonData> data = infoOmMegRepository.hentSituasjonHistorikk(new AktorId(eksternIdent));

        assertEquals(svar2.getAlternativId(), data.get(0).getAlternativId());
        assertEquals(svar1.getAlternativId(), data.get(1).getAlternativId());
    }

    private FremtidigSituasjonData lagreFremtidigSituasjon(FremtidigSituasjonSvar svarId, String tekst, String endretAv, String ident){
        FremtidigSituasjonData data = new FremtidigSituasjonData()
                .setAlternativId(svarId)
                .setTekst(tekst);

        infoOmMegRepository.lagreFremtidigSituasjonForAktorId(data, new AktorId(ident), endretAv);
        return data;
    }


}
