package no.nav.veilarbvedtakinfo.config;

import no.nav.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.service.BehovsvurderingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidSitasjonService.class,
        BehovsvurderingService.class,
        AuthService.class
})
public class ServiceTestConfig { }
