package no.nav.fo.veilarbvedtakinfo;

import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseUtils;
import no.nav.fo.veilarbvedtakinfo.db.DbTestUtils;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalSvar;
import no.nav.fo.veilarbvedtakinfo.mock.RegistreringClientMock;
import no.nav.fo.veilarbvedtakinfo.resources.InfoOmMegResource;

import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.lang.System.setProperty;
import static no.nav.fo.veilarbvedtakinfo.db.DbTestUtils.setupInMemoryContext;
import static no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient.VEILARBREGISTRERING_URL_PROPERTY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegResourceIntegrationTest {

    private static JdbcTemplate db;
    private static InfoOmMegResource infoOmMegResource;
    private static UserService userService;

    public static String eksernIdent = "123";
    public static String eksernIdent2 = "543";

    @BeforeEach
    public void setup() {
        setupInMemoryContext();

        setProperty(VEILARBREGISTRERING_URL_PROPERTY_NAME, "MOCKED_URL");

        db = DbTestUtils.getTestDb();
        DatabaseUtils.createTables(db);

        userService = mock(UserService.class);
        infoOmMegResource = new InfoOmMegResource(
                new InfoOmMegService(new InfoOmMegRepository(db), new RegistreringClientMock()),
                userService,
                mock(AktorService.class),
                mock(PepClient.class)
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
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));

        HovedmalData actualData = infoOmMegResource.oppdaterFremtidigSituasjon(data);


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
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));

        HovedmalData actualData = infoOmMegResource.oppdaterFremtidigSituasjon(data);

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
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent);

        infoOmMegResource.oppdaterFremtidigSituasjon(svar1);
        infoOmMegResource.oppdaterFremtidigSituasjon(svar2);

        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent2));
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent2);

        infoOmMegResource.oppdaterFremtidigSituasjon(svar3);
        List<HovedmalData> data = infoOmMegResource.hentSituasjonListe();

        assertEquals(1, data.size());
    }

}
