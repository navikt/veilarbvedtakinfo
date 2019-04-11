package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.config.ApiAppConfigurator;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationTestConfig extends ApplicationConfig {
    public static final boolean RUN_WITH_MOCKS = true;

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator.sts(); //todo: bør endres på sikt slik at bruker logges inn vha devproxy.
    }

}
