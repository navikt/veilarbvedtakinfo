package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.resources.InfoOmMegResource;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;


import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

class ServiceBeans {
    @Bean
    InfoOmMegService infoOmMegService(
            InfoOmMegRepository infoOmMegRepository
    ) {
        return new InfoOmMegService(
                infoOmMegRepository
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

}
