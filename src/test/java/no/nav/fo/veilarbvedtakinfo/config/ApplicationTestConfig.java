package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.fo.veilarbvedtakinfo.mock.UserServiceMock;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class ApplicationTestConfig extends ApplicationConfig {
    public static final boolean RUN_WITH_MOCKS = true;

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

        if (RUN_WITH_MOCKS) {
            apiAppConfigurator.sts(); //todo: bør endres på sikt slik at bruker logges inn vha devproxy.
        } else {
            apiAppConfigurator.azureADB2CLogin().sts();
        }
    }

    @Bean
    public UserService userService(Provider<HttpServletRequest> requestProvider){
        return new UserServiceMock(requestProvider);
    }

}
