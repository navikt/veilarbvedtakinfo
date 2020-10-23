package no.nav.fo.veilarbvedtakinfo.controller;

import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.test.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.lang.System.setProperty;
import static no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClientImpl.VEILARBREGISTRERING_URL_PROPERTY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegControllerIntegrationTest {

    private JdbcTemplate db;
    private InfoOmMegController infoOmMegController;
    private RegistreringClient registreringClient;
    private AuthService authService;

    public static AktorId eksernIdent = AktorId.of("123");
    public static AktorId eksernIdent2 = AktorId.of("543");

    @BeforeEach
    public void setup() {
        setProperty(VEILARBREGISTRERING_URL_PROPERTY_NAME, "MOCKED_URL");

        db = DbTestUtils.getTestDb();

        registreringClient = mock(RegistreringClient.class);
        authService = mock(AuthService.class);

        infoOmMegController = new InfoOmMegController(
                new InfoOmMegService(new InfoOmMegRepository(db), registreringClient),
                authService
        );
    }

    @AfterEach
    public void tearDown() {
        DbTestUtils.cleanUp(db, eksernIdent);
        DbTestUtils.cleanUp(db, eksernIdent2);
    }

    @Test
    public void oppdaterFremtidigSituasjon_skalSetteProperties() {
        HovedmalData data = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER)
                .setTekst("Test1");

        when(authService.erEksternBruker()).thenReturn(true);
        when(authService.hentInnloggetSubject()).thenReturn("12345");
        when(authService.hentAktorId(eq(Fnr.of("12345")))).thenReturn(eksernIdent);

        HovedmalData actualData = infoOmMegController.oppdaterFremtidigSituasjon(data, Fnr.of("12345"));

        assertNotEquals(null, actualData.getDato());
        assertEquals(data.getAlternativId(), actualData.getAlternativId());
        assertEquals(EndretAvType.BRUKER, actualData.getEndretAv());
        assertEquals(data.getTekst(), actualData.getTekst());
    }

    @Test
    public void oppdaterFremtidigSituasjon_veileder_skalSetteProperties() {
        HovedmalData data = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER);

        when(authService.erEksternBruker()).thenReturn(false);
        when(authService.hentInnloggetSubject()).thenReturn("Z123");
        when(authService.hentAktorId(any())).thenReturn(eksernIdent);

        HovedmalData actualData = infoOmMegController.oppdaterFremtidigSituasjon(data, Fnr.of("1234"));

        assertEquals(EndretAvType.VEILEDER, actualData.getEndretAv());
    }

    @Test
    public void hentSituasjonHistorikk_flere_brukere_success() {

        HovedmalData svar1 = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER)
                .setTekst("Test1");

        HovedmalData svar2 = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER)
                .setTekst("Test2");

        HovedmalData svar3 = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER)
                .setTekst("Test3");

        when(authService.erEksternBruker()).thenReturn(true);
        when(authService.hentAktorId(any())).thenReturn(eksernIdent);
        when(authService.hentInnloggetSubject()).thenReturn(eksernIdent.get());

        infoOmMegController.oppdaterFremtidigSituasjon(svar1, null);
        infoOmMegController.oppdaterFremtidigSituasjon(svar2, null);

        when(authService.hentAktorId(any())).thenReturn(eksernIdent2);
        when(authService.hentInnloggetSubject()).thenReturn(eksernIdent2.get());

        infoOmMegController.oppdaterFremtidigSituasjon(svar3, null);
        List<HovedmalData> data = infoOmMegController.hentSituasjonListe(null);

        assertEquals(1, data.size());
    }

}
