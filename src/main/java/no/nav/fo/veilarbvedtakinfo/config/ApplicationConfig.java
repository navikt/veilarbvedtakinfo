package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ServiceBeans.class)

public class ApplicationConfig implements ApiApplication {

    public static final String APPLICATION_NAME = "veilarbvedtakinfo";

    @Override
    public String getContextPath() {
        return "/veilarbvedtakinfo";
    }
    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }

}
