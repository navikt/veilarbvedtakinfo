package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.controller.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidsSituasjonController.class,
        BehovsvurderingController.class,
        InfoOmMegController.class,
        MotestotteController.class,
})
public class ControllerTestConfig {}
