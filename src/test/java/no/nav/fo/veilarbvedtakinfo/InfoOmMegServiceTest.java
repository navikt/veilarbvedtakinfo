package no.nav.fo.veilarbvedtakinfo;

import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.*;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.*;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegServiceTest {
    private static InfoOmMegService infoOmMegService;
    private static InfoOmMegRepository infoOmMegRepository;
    private static RegistreringClient registreringClient;
    private final static String brukerIdent = "1234";

    @BeforeEach
    void setup() {
        infoOmMegRepository = mock(InfoOmMegRepository.class);
        registreringClient = mock(RegistreringClient.class);
        infoOmMegService = new InfoOmMegService(infoOmMegRepository, registreringClient);

    }

    @Test
    void hentFremtidigSituasjon_manglerRegistrering() {
        Date now = Date.from(Instant.now());
        EndretAvType endretAv = EndretAvType.VEILEDER;
        HovedmalData fremtidigSituasjon = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER)
                .setTekst("Tekst")
                .setDato(now)
                .setEndretAv(endretAv);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(null);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(fremtidigSituasjon);

        HovedmalData hovedmal = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(fremtidigSituasjon.getAlternativId(), hovedmal.getAlternativId());
        assertEquals(fremtidigSituasjon.getTekst(), hovedmal.getTekst());
        assertEquals(fremtidigSituasjon.getDato(), hovedmal.getDato());
        assertEquals(fremtidigSituasjon.getEndretAv(), endretAv);

    }

    @Test
    void hentFremtidigSituasjon_manglerHovedmal() {
        Date now = Date.from(Instant.now());
        Besvarelse besvarelse = new Besvarelse()
                .setFremtidigSituasjon(FremtidigSituasjonSvar.SAMME_ARBEIDSGIVER);
        BrukerRegistrering brukerRegistrering = new BrukerRegistrering()
                .setBesvarelse(besvarelse)
                .setOpprettetDato(now);
        BrukerRegistreringWrapper registrering = new BrukerRegistreringWrapper()
                .setRegistrering(brukerRegistrering);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(registrering);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(null);

        HovedmalData hovedmal = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(registrering.getRegistrering().getBesvarelse().getFremtidigSituasjon().name(), hovedmal.getAlternativId().name());
        assertEquals(registrering.getRegistrering().getOpprettetDato(), hovedmal.getDato());
        assertEquals(EndretAvType.BRUKER, hovedmal.getEndretAv());

    }

    @Test
    void hentFremtidigSituasjon_manglerHovedmal_manglerFremtidigsituasjon() {
        when(registreringClient.hentSisteRegistrering(any())).thenReturn(null);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(null);

        HovedmalData hovedmal = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertNull(hovedmal);
    }

    @Test
    void hentFremtidigSituasjon_nyereRegistrering_medFremtidigSituasjon() {
        Date hovedmalDate = DateUtils.addMinutes(new Date(), -1);
        Date registreringDate = Date.from(Instant.now());
        HovedmalData hovedmal = new HovedmalData()
                .setAlternativId(HovedmalSvar.SAMME_ARBEIDSGIVER_NY_STILLING)
                .setDato(hovedmalDate);

        Besvarelse besvarelse = new Besvarelse()
                .setFremtidigSituasjon(FremtidigSituasjonSvar.SAMME_ARBEIDSGIVER);

        BrukerRegistrering brukerRegistrering = new BrukerRegistrering()
                .setBesvarelse(besvarelse)
                .setOpprettetDato(registreringDate);

        BrukerRegistreringWrapper registrering = new BrukerRegistreringWrapper()
                .setRegistrering(brukerRegistrering);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(registrering);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(hovedmal);

        HovedmalData hovedmalData = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(registrering.getRegistrering().getBesvarelse().getFremtidigSituasjon().name(), hovedmalData.getAlternativId().name());
        assertEquals(registrering.getRegistrering().getOpprettetDato(), hovedmalData.getDato());

    }

    @Test
    void hentFremtidigSituasjon_nyereRegistrering_utenFremtidigSituasjon() {
        Date hovedmalDate = DateUtils.addMinutes(new Date(), -1);
        Date registreringDate = Date.from(Instant.now());
        HovedmalData hovedmal = new HovedmalData()
                .setAlternativId(HovedmalSvar.SAMME_ARBEIDSGIVER)
                .setDato(hovedmalDate);

        Besvarelse besvarelse = new Besvarelse()
                .setFremtidigSituasjon(null);

        BrukerRegistrering brukerRegistrering = new BrukerRegistrering()
                .setBesvarelse(besvarelse)
                .setOpprettetDato(registreringDate);

        BrukerRegistreringWrapper registrering = new BrukerRegistreringWrapper()
                .setRegistrering(brukerRegistrering);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(registrering);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(hovedmal);

        HovedmalData hovedmalData = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(HovedmalSvar.IKKE_OPPGITT, hovedmalData.getAlternativId());
        assertEquals(registreringDate, hovedmalData.getDato());

    }

    @Test
    void hentFremtidigSituasjon_nyereRegistrering_USIKKER() {
        Date hovedmalDate = DateUtils.addMinutes(new Date(), -1);
        Date registreringDate = Date.from(Instant.now());
        HovedmalData hovedmal = new HovedmalData()
                .setAlternativId(HovedmalSvar.SAMME_ARBEIDSGIVER_NY_STILLING)
                .setDato(hovedmalDate);

        Besvarelse besvarelse = new Besvarelse()
                .setFremtidigSituasjon(FremtidigSituasjonSvar.USIKKER);

        BrukerRegistrering brukerRegistrering = new BrukerRegistrering()
                .setBesvarelse(besvarelse)
                .setOpprettetDato(registreringDate);

        BrukerRegistreringWrapper registrering = new BrukerRegistreringWrapper()
                .setRegistrering(brukerRegistrering);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(registrering);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(hovedmal);

        HovedmalData hovedmalData = infoOmMegService.hentFremtidigSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(HovedmalSvar.IKKE_OPPGITT, hovedmalData.getAlternativId());

    }

    @Test
    void hentSisteSituasjon() {
        Date now = Date.from(Instant.now());
        EndretAvType endretAv = EndretAvType.VEILEDER;
        HovedmalData fremtidigSituasjon = new HovedmalData()
                .setAlternativId(HovedmalSvar.SAMME_ARBEIDSGIVER)
                .setTekst("Tekst")
                .setDato(now)
                .setEndretAv(endretAv);

        HelseOgAndreHensynData helsehinder = new HelseOgAndreHensynData().setVerdi(HelseHinderSvar.JA).setDato(now);
        HelseOgAndreHensynData andrehinder = new HelseOgAndreHensynData().setVerdi(HelseHinderSvar.JA).setDato(now);

        when(registreringClient.hentSisteRegistrering(any())).thenReturn(null);
        when(infoOmMegRepository.hentFremtidigSituasjonForAktorId(any())).thenReturn(fremtidigSituasjon);
        when(infoOmMegRepository.hentHelseHinderForAktorId(any())).thenReturn(helsehinder);
        when(infoOmMegRepository.hentAndreHinderForAktorId(any())).thenReturn(andrehinder);

        InfoOmMegData sisteSituasjon = infoOmMegService.hentSisteSituasjon(new AktorId(brukerIdent), brukerIdent);

        assertEquals(fremtidigSituasjon.getAlternativId(), sisteSituasjon.getFremtidigSituasjonData().getAlternativId());

        assertEquals(helsehinder.getVerdi(), sisteSituasjon.getHelseHinder().getVerdi());
        assertEquals(helsehinder.getDato(), sisteSituasjon.getHelseHinder().getDato());

        assertEquals(andrehinder.getVerdi(), sisteSituasjon.getAndreHinder().getVerdi());
        assertEquals(andrehinder.getDato(), sisteSituasjon.getAndreHinder().getDato());

    }

    @Test
    void hentHelsehinder() {
        Date now = Date.from(Instant.now());
        Date earlier = DateUtils.addMinutes(new Date(), -1);

        HelseOgAndreHensynData helseHinder = new HelseOgAndreHensynData()
                .setDato(earlier)
                .setVerdi(HelseHinderSvar.NEI);

        BrukerRegistreringWrapper registreringWrapper = byggRegistreringsWrapper(now, HelseHinderSvar.JA);

        when(infoOmMegRepository.hentHelseHinderForAktorId(any())).thenReturn(helseHinder);
        when(registreringClient.hentSisteRegistrering(any())).thenReturn(registreringWrapper);

        HelseOgAndreHensynData helseHinderData = infoOmMegService.hentHelseHinder(new AktorId(brukerIdent), brukerIdent);

        assertEquals(HelseHinderSvar.JA, helseHinderData.getVerdi());
        assertEquals(now, helseHinderData.getDato());

    }

    private BrukerRegistreringWrapper byggRegistreringsWrapper(Date dato, HelseHinderSvar svar) {
        Besvarelse besvarelse = new Besvarelse().setHelseHinder(svar);
        BrukerRegistrering brukerRegistrering = new BrukerRegistrering()
                .setBesvarelse(besvarelse)
                .setOpprettetDato(dato);
        return new BrukerRegistreringWrapper().setRegistrering(brukerRegistrering);
    }
}
