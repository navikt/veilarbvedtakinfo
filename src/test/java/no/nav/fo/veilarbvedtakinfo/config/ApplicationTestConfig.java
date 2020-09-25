package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.mock.AktorregisterClientMock;
import no.nav.fo.veilarbvedtakinfo.mock.Mock;
import no.nav.fo.veilarbvedtakinfo.mock.PepClientMock;
import no.nav.fo.veilarbvedtakinfo.mock.UserServiceMock;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
@Import({
        SwaggerConfig.class,
        ClientTestConfig.class,
        ServiceTestConfig.class,
        FilterTestConfig.class,
        ControllerTestConfig.class,
        RepositoryTestConfig.class,
        HealthConfig.class
})

public class ApplicationTestConfig {
    public static final boolean RUN_WITH_MOCKS = true;

    @Bean
    @Conditional(Mock.class)
    public UserService userService(ObjectProvider<HttpServletRequest> requestProvider, Pep pep){
        return new UserServiceMock(requestProvider, pep);
    }

    @Bean
    @Conditional(Mock.class)
    public AktorregisterClient aktorService(){
        return new AktorregisterClientMock();
    }

    @Bean
    @Conditional(Mock.class)
    public Pep veilarbPep(){
        return new PepClientMock();
    }
}
