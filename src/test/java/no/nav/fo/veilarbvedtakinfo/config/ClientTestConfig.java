package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import org.springframework.context.annotation.Bean;

public class ClientTestConfig {

    @Bean
    public RegistreringClient registreringClient() {
        return new RegistreringClient() {

            @Override
            public BrukerRegistreringWrapper hentSisteRegistrering(String fnr) {return null;}

            @Override
            public HealthCheckResult checkHealth() {
                return null;
            }
        };
    }
}
