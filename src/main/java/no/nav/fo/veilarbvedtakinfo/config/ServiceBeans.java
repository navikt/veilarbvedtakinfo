package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.resources.InfoOmMegResource;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.context.annotation.Bean;


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
    InfoOmMegResource infoOmMegResource() {
        return new InfoOmMegResource();
    }

    @Bean
    InfoOmMegRepository infoOmMegRepository() {
        return new InfoOmMegRepository();
    }

    @Bean
    UserService userService(Provider<HttpServletRequest> provider) {
        return new UserService(provider);
    }

}
