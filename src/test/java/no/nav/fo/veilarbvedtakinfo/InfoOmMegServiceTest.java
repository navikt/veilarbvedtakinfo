package no.nav.fo.veilarbvedtakinfo;

import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.InfoOmMegData;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegServiceTest {
    private static InfoOmMegService infoOmMegService;
    private static InfoOmMegRepository infoOmMegRepository;

    @BeforeEach
    void setup() {
        infoOmMegRepository = mock(InfoOmMegRepository.class);
        infoOmMegService = new InfoOmMegService(infoOmMegRepository);

    }

    @Test
    void hentSisteSituasjon() {
        Date now = Date.from(Instant.now());
        String endretAv = "VEILEDER";
        FremtidigSituasjonData fremtidigSituasjon = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.USIKKER)
                .setTekst("Tekst")
                .setDato(now)
                .setEndretAv(endretAv);

        HelseOgAndreHensynData helsehinder = new HelseOgAndreHensynData().setVerdi(true).setDato(now);
        HelseOgAndreHensynData andrehinder = new HelseOgAndreHensynData().setVerdi(true).setDato(now);

        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(fremtidigSituasjon);
        when(infoOmMegRepository.hentHelseHinderForAktorId(any())).thenReturn(helsehinder);
        when(infoOmMegRepository.hentAndreHinderForAktorId(any())).thenReturn(andrehinder);

        InfoOmMegData sisteSituasjon = infoOmMegService.hentSisteSituasjon(any());

        assertEquals(fremtidigSituasjon.getAlternativId(), sisteSituasjon.getFremtidigSituasjonData().getAlternativId());
        assertEquals(fremtidigSituasjon.getTekst(), sisteSituasjon.getFremtidigSituasjonData().getTekst());
        assertEquals(fremtidigSituasjon.getDato(), sisteSituasjon.getFremtidigSituasjonData().getDato());
        assertEquals(fremtidigSituasjon.getEndretAv(), endretAv);

        assertEquals(helsehinder.isVerdi(), sisteSituasjon.getHelseHinder().isVerdi());
        assertEquals(helsehinder.getDato(), sisteSituasjon.getHelseHinder().getDato());

        assertEquals(andrehinder.isVerdi(), sisteSituasjon.getAndreHinder().isVerdi());
        assertEquals(andrehinder.getDato(), sisteSituasjon.getAndreHinder().getDato());

    }
}
