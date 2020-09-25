package no.nav.fo.veilarbvedtakinfo.test;

import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.controller.InfoOmMegController;
import no.nav.fo.veilarbvedtakinfo.db.DbTestUtils;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.mock.RegistreringClientMock;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegControllerIntegrationTest {

    private static JdbcTemplate db;
    private static InfoOmMegController infoOmMegController;
    private static AktorregisterClient aktorregisterClient;

    public static String eksernIdent = "123";
    public static String eksernIdent2 = "543";

    @BeforeEach
    public void setup() {
        setProperty(VEILARBREGISTRERING_URL_PROPERTY_NAME, "MOCKED_URL");

        db = DbTestUtils.getTestDb();

        aktorregisterClient = mock(AktorregisterClient.class);
        infoOmMegController = new InfoOmMegController(
                new InfoOmMegService(new InfoOmMegRepository(db), new RegistreringClientMock()),
                mock(AuthService.class)
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

        when(userService.erEksternBruker()).thenReturn(true);
        when(aktorregisterClient.hentAktorId((String) any())).thenReturn(eksernIdent);

        HovedmalData actualData = infoOmMegController.oppdaterFremtidigSituasjon(data);


        assertNotEquals(null, actualData.getDato());
        assertEquals(data.getAlternativId(), actualData.getAlternativId());
        assertEquals(EndretAvType.BRUKER, actualData.getEndretAv());
        assertEquals(data.getTekst(), actualData.getTekst());
    }

    @Test
    public void oppdaterFremtidigSituasjon_veileder_skalSetteProperties() {
        HovedmalData data = new HovedmalData()
                .setAlternativId(HovedmalSvar.NY_ARBEIDSGIVER);

        when(userService.erEksternBruker()).thenReturn(false);
        when(userService.getUid()).thenReturn("Z123");
        when(aktorregisterClient.hentAktorId((String) any())).thenReturn(eksernIdent);

        HovedmalData actualData = infoOmMegController.oppdaterFremtidigSituasjon(data);

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

        when(userService.erEksternBruker()).thenReturn(true);
        when(aktorregisterClient.hentAktorId((String) any())).thenReturn(eksernIdent);
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent);

        infoOmMegController.oppdaterFremtidigSituasjon(svar1);
        infoOmMegController.oppdaterFremtidigSituasjon(svar2);

        when(aktorregisterClient.hentAktorId((String) any())).thenReturn(eksernIdent2);
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent2);

        infoOmMegController.oppdaterFremtidigSituasjon(svar3);
        List<HovedmalData> data = infoOmMegController.hentSituasjonListe();

        assertEquals(1, data.size());
    }

}
