package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.resources.BehovsvurderingResource;
import no.nav.fo.veilarbvedtakinfo.resources.InfoOmMegResource;
import no.nav.fo.veilarbvedtakinfo.resources.MotestotteResource;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

class ServiceBeans {
    @Bean
    InfoOmMegService infoOmMegService(
            InfoOmMegRepository infoOmMegRepository,
            RegistreringClient registreringClient
    ) {
        return new InfoOmMegService(
                infoOmMegRepository,
                registreringClient
        );
    }

    @Bean
    InfoOmMegResource infoOmMegResource(InfoOmMegService infoOmMegService,
                                        UserService userService,
                                        AktorService aktorService,
                                        PepClient pepClient) {
        return new InfoOmMegResource(infoOmMegService, userService, aktorService, pepClient);
    }

    @Bean
    InfoOmMegRepository infoOmMegRepository(JdbcTemplate db) {
        return new InfoOmMegRepository(db);
    }

    @Bean
    UserService userService(Provider<HttpServletRequest> provider) {
        return new UserService(provider);
    }

    @Bean
    RegistreringClient registreringClient(Provider<HttpServletRequest> provider) {
        return new RegistreringClient(provider);
    }
    @Bean
    BehovsvurderingResource behovsvurderingResource(BehovsvurderingService behovsvurderingService,
                                                    UserService userService,
                                                    AktorService aktorService,
                                                    PepClient pepClient) {
        return new BehovsvurderingResource(behovsvurderingService, userService, aktorService, pepClient);
    }

    @Bean
    BehovsvurderingService behovsvurderingService(BehovsvurderingRepository behovsvurderingRepository) {
        return new BehovsvurderingService(behovsvurderingRepository);
    }

    @Bean
    BehovsvurderingRepository behovsvurderingRepository(JdbcTemplate db) {
        return new BehovsvurderingRepository(db);
    }


    @Bean
    MotestotteResource motestotteResource(MotestotteRepository motestotteRepository,
                                          UserService userService,
                                          AktorService aktorService,
                                          PepClient pepClient) {
        return new MotestotteResource(motestotteRepository, userService, aktorService, pepClient);
    }

    @Bean
    MotestotteRepository motestotteRepository(JdbcTemplate db) {
        return new MotestotteRepository(db);
    }

}
