package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HinderSvar;
import no.nav.fo.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.Assert.*;

public class InfoOmMegRepositoryTest {

    private static JdbcTemplate db;
    private static InfoOmMegRepository infoOmMegRepository;
    private static AktorId eksternIdent = AktorId.of("123");

    @BeforeEach
    public void setup() {
        db = DbTestUtils.getTestDb();
        infoOmMegRepository = new InfoOmMegRepository(db);
    }

    @AfterEach
    public void tearDown() {
        DbTestUtils.cleanUp(db, eksternIdent);
    }

    @Test
    public void hentFremtidigSituasjon_empty_success() {
       HovedmalData data = infoOmMegRepository.hentFremtidigSituasjonForAktorId(eksternIdent);
       assertNull(data);
    }

    @Test
    public void hentSituasjonHistorikk_empty_success() {
        List<HovedmalData> data = infoOmMegRepository.hentSituasjonHistorikk(eksternIdent);
        assertEquals(0, data.size());
    }

    @Test
    public void lagreFremtidigSituasjonForAktorId_success() {
        AktorId aktorId = eksternIdent;
        HovedmalData data = new HovedmalData()
            .setAlternativId(HovedmalSvar.SAMME_ARBEIDSGIVER)
            .setTekst("Test");

        long id = infoOmMegRepository.lagreFremtidigSituasjonForAktorId(data, aktorId, eksternIdent.get());
        HovedmalData actual = infoOmMegRepository.hentFremtidigSituasjonForId(id);

        assertEquals(data.getAlternativId(), actual.getAlternativId());
        assertEquals(data.getTekst(), actual.getTekst());
        assertEquals(EndretAvType.BRUKER, actual.getEndretAv());
        assertNotEquals(null, actual.getDato());
    }

    @Test
    public void hentSituasjonHistorikk_non_empty_success() throws InterruptedException {
        HovedmalData svar1 = lagreFremtidigSituasjon(HovedmalSvar.NY_ARBEIDSGIVER, "Svar1", eksternIdent.get());

        Thread.sleep(5); //For å unngå identisk dato

        HovedmalData svar2 = lagreFremtidigSituasjon(HovedmalSvar.SAMME_ARBEIDSGIVER_NY_STILLING, "Svar2", eksternIdent.get());

        List<HovedmalData> data = infoOmMegRepository.hentSituasjonHistorikk(eksternIdent);

        assertEquals(svar2.getAlternativId(), data.get(0).getAlternativId());
        assertEquals(svar1.getAlternativId(), data.get(1).getAlternativId());
    }

    private HovedmalData lagreFremtidigSituasjon(HovedmalSvar svarId, String tekst, String endretAv){
        HovedmalData data = new HovedmalData()
                .setAlternativId(svarId)
                .setTekst(tekst);

        infoOmMegRepository.lagreFremtidigSituasjonForAktorId(data, eksternIdent, endretAv);
        return data;
    }

    @Test
    public void lagreHelseHinderForAktorId_success() {
        HelseOgAndreHensynData data = new HelseOgAndreHensynData()
                .setVerdi(HinderSvar.NEI);

        long id = infoOmMegRepository.lagreHelseHinderForAktorId(data, eksternIdent);
        HelseOgAndreHensynData actual = infoOmMegRepository.hentHelseHinderForId(id);

        assertEquals(data.getVerdi(), actual.getVerdi());
        assertNotNull(actual.getDato());
    }

    @Test
    public void lagreAndreHinderForAktorId_success() {
        HelseOgAndreHensynData data = new HelseOgAndreHensynData()
                .setVerdi(HinderSvar.NEI);

        long id = infoOmMegRepository.lagreAndreHinderForAktorId(data, eksternIdent);
        HelseOgAndreHensynData actual = infoOmMegRepository.hentAndreHinderForId(id);

        assertEquals(data.getVerdi(), actual.getVerdi());
        assertNotNull(actual.getDato());
    }

}
