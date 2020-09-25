package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.controller.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidsSituasjonResource.class,
        BehovsvurderingResource.class,
        InfoOmMegResource.class,
        MotestotteResource.class,
})
public class ControllerTestConfig {}
