package no.nav.veilarbvedtakinfo.config;

import no.nav.veilarbvedtakinfo.controller.v1.BehovsvurderingController;
import no.nav.veilarbvedtakinfo.controller.v1.InternalController;
import no.nav.veilarbvedtakinfo.controller.v1.MotestotteController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BehovsvurderingController.class,
        MotestotteController.class,
        InternalController.class
})
public class ControllerTestConfig {}
