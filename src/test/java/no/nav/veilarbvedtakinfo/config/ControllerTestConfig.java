package no.nav.veilarbvedtakinfo.config;

import no.nav.veilarbvedtakinfo.controller.ArbeidsSituasjonController;
import no.nav.veilarbvedtakinfo.controller.BehovsvurderingController;
import no.nav.veilarbvedtakinfo.controller.InternalController;
import no.nav.veilarbvedtakinfo.controller.MotestotteController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ArbeidsSituasjonController.class,
        BehovsvurderingController.class,
        MotestotteController.class,
        InternalController.class
})
public class ControllerTestConfig {}
