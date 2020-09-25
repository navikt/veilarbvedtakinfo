package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.service.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidSitasjonService.class,
        BehovsvurderingService.class,
        InfoOmMegService.class,
        UserService.class
})
public class ServiceTestConfig { }
