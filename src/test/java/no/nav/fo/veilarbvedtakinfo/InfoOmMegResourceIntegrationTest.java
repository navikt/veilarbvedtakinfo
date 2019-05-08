package no.nav.fo.veilarbvedtakinfo;

import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseUtils;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.resources.InfoOmMegResource;

import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static no.nav.fo.veilarbvedtakinfo.db.DatabaseTestContext.setupInMemoryContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InfoOmMegResourceIntegrationTest {

    private static AnnotationConfigApplicationContext context;
    private static InfoOmMegResource infoOmMegResource;
    private static UserService userService;
    private static InfoOmMegRepository infoOmMegRepository;

    public static String eksernIdent = "123";
    public static String eksernIdent2 = "543";

    @BeforeEach
    public void setup() {
        setupInMemoryContext();
        TestContext.setup();
        context = new AnnotationConfigApplicationContext(
                InfoOmMegConfigTest.class
        );

        context.start();

        DatabaseUtils.createTables((JdbcTemplate) context.getBean("jdbcTemplate"));
        infoOmMegResource = context.getBean(InfoOmMegResource.class);
        userService = context.getBean(UserService.class);
        infoOmMegRepository = context.getBean(InfoOmMegRepository.class);
    }

    @AfterEach
    public void tearDown() {
        infoOmMegRepository.cleanUp(eksernIdent);
        infoOmMegRepository.cleanUp(eksernIdent2);

        context.stop();

    }

    @Test
    public void oppdaterFremtidigSituasjon_skalSetteProperties() {
        FremtidigSituasjonData data = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                .setTekst("Test1");

        when(userService.erEksternBruker()).thenReturn(true);
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));

        FremtidigSituasjonData actualData = infoOmMegResource.oppdaterFremtidigSituasjon(data);


        assertNotEquals(null, actualData.getDato());
        assertEquals(data.getAlternativId(), actualData.getAlternativId());
        assertEquals("BRUKER", actualData.getEndretAv());
        assertEquals(data.getTekst(), actualData.getTekst());
    }

    @Test
    public void oppdaterFremtidigSituasjon_veileder_skalSetteProperties() {
        FremtidigSituasjonData data = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.NY_ARBEIDSGIVER);

        when(userService.erEksternBruker()).thenReturn(false);
        when(userService.getUid()).thenReturn("Z123");
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));

        FremtidigSituasjonData actualData = infoOmMegResource.oppdaterFremtidigSituasjon(data);

        assertEquals("VEILEDER", actualData.getEndretAv());

    }

    @Test
    public void hentSituasjonHistorikk_flere_brukere_success() {

        FremtidigSituasjonData svar1 = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                .setTekst("Test1");

        FremtidigSituasjonData svar2 = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                .setTekst("Test2");

        FremtidigSituasjonData svar3 = new FremtidigSituasjonData()
                .setAlternativId(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                .setTekst("Test3");

        when(userService.erEksternBruker()).thenReturn(true);
        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent));
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent);

        infoOmMegResource.oppdaterFremtidigSituasjon(svar1);
        infoOmMegResource.oppdaterFremtidigSituasjon(svar2);

        when(userService.getAktorIdOrElseThrow(any(), any())).thenReturn(new AktorId(eksernIdent2));
        when(userService.hentFnrFraUrlEllerToken()).thenReturn(eksernIdent2);

        infoOmMegResource.oppdaterFremtidigSituasjon(svar3);
        List<FremtidigSituasjonData> data = infoOmMegResource.hentSituasjonListe();

        assertEquals(1, data.size());
    }

    @Configuration
    @ComponentScan
    public static class InfoOmMegConfigTest {

        @Bean
        public AktorService aktoerService() {
            return mock(AktorService.class);
        }

        @Bean
        PepClient pepClient() {
            return mock(PepClient.class);
        }

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }

    }

}
