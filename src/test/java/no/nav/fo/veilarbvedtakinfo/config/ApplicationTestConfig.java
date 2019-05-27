package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.mock.*;
import no.nav.fo.veilarbvedtakinfo.service.OppdatertService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class ApplicationTestConfig extends ApplicationConfig {
    public static final boolean RUN_WITH_MOCKS = true;

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator.sts();
    }

    @Bean
    @Conditional(Mock.class)
    public UserService userService(Provider<HttpServletRequest> requestProvider){
        return new UserServiceMock(requestProvider);
    }

    @Bean
    @Conditional(Mock.class)
    public RegistreringClient registreringClient(Provider<HttpServletRequest> requestProvider){
        return new RegistreringClientMock(requestProvider);
    }

    @Bean
    @Conditional(Mock.class)
    public AktorService aktorService(){
        return new AktorServiceMock();
    }

    @Bean
    @Conditional(Mock.class)
    public PepClient pepClient(){
        return new PepClientMock();
    }

    @Bean
    public OppdatertService oppdatertService() {
        return new OppdatertService(KafkaConfig.kafkaProducer());
    }
}
