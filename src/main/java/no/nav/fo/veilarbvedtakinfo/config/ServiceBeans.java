package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.sts.SystemUserTokenProvider;
import no.nav.fo.veilarbvedtakinfo.db.ArbeidSitasjonRepository;
import no.nav.fo.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.controller.ArbeidsSituasjonResource;
import no.nav.fo.veilarbvedtakinfo.controller.BehovsvurderingResource;
import no.nav.fo.veilarbvedtakinfo.controller.InfoOmMegResource;
import no.nav.fo.veilarbvedtakinfo.controller.MotestotteResource;
import no.nav.fo.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
class ServiceBeans {

    @Bean
    InfoOmMegService infoOmMegService(InfoOmMegRepository infoOmMegRepository,
                                      RegistreringClient registreringClient) {
        return new InfoOmMegService(infoOmMegRepository,registreringClient);
    }

    @Bean
    InfoOmMegResource infoOmMegResource(InfoOmMegService infoOmMegService,
                                        UserService userService,
                                        AktorregisterClient aktorregisterClient,
                                        Pep pep) {
        return new InfoOmMegResource(infoOmMegService, userService, aktorregisterClient, pep);
    }

    @Bean
    InfoOmMegRepository infoOmMegRepository(JdbcTemplate db) {
        return new InfoOmMegRepository(db);
    }

    @Bean
    UserService userService(ObjectProvider<HttpServletRequest> provider, Pep pep) {
        return new UserService(provider, pep);
    }

    @Bean
    RegistreringClient registreringClient(HttpServletRequest httpServletRequest, SystemUserTokenProvider systemUserTokenProvider) {
        return new RegistreringClient(httpServletRequest, systemUserTokenProvider);
    }

    @Bean
    BehovsvurderingResource behovsvurderingResource(BehovsvurderingService behovsvurderingService,
                                                    UserService userService,
                                                    AktorregisterClient aktorregisterClient,
                                                    Pep pep) {
        return new BehovsvurderingResource(behovsvurderingService, userService, aktorregisterClient, pep);
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
    MotestotteRepository motestotteRepository(JdbcTemplate db) {
        return new MotestotteRepository(db);
    }

    @Bean
    MotestotteResource motestotteResource(MotestotteRepository motestotteRepository,
                                          UserService userService,
                                          AktorregisterClient aktorregisterClient,
                                          Pep pep) {
        return new MotestotteResource(motestotteRepository, userService, aktorregisterClient, pep);
    }


    @Bean
    ArbeidSitasjonRepository arbeidSitasjonRepository(JdbcTemplate db) {
        return new ArbeidSitasjonRepository(db);
    }

    @Bean
    ArbeidSitasjonService arbeidSitasjonService(ArbeidSitasjonRepository repository) {
        return new ArbeidSitasjonService(repository);
    }

    @Bean
    ArbeidsSituasjonResource arbeidsSituasjonResource(ArbeidSitasjonService arbeidSitasjonService,
                                          UserService userService,
                                          AktorregisterClient aktorregisterClient,
                                          Pep pep) {
        return new ArbeidsSituasjonResource(arbeidSitasjonService, userService, aktorregisterClient, pep);
    }
}
