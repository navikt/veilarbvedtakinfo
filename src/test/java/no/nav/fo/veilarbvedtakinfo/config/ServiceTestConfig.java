package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidSitasjonService.class,
        BehovsvurderingService.class,
        AuthService.class
})
public class ServiceTestConfig { }
