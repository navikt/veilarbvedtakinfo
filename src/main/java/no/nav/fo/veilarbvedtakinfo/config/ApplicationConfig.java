package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPep;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.client.aktorregister.AktorregisterHttpClient;
import no.nav.common.client.aktorregister.CachedAktorregisterClient;
import no.nav.common.sts.NaisSystemUserTokenProvider;
import no.nav.common.sts.OpenAmSystemUserTokenProvider;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.NaisUtils;
import no.nav.fo.veilarbvedtakinfo.client.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.client.RegistreringClientImpl;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.NaisUtils.getCredentials;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
public class ApplicationConfig {

    public static final String APPLICATION_NAME = "veilarbvedtakinfo";

    @Bean
    public Credentials serviceUserCredentials() {
        return getCredentials("service_user");
    }

    @Bean
    public OpenAmSystemUserTokenProvider openAmsystemUserTokenProvider(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return new OpenAmSystemUserTokenProvider(
                properties.getOpenAmDiscoveryUrl(), properties.getOpenAmRedirectUrl(),
                new Credentials(properties.getOpenAmIssoRpUsername(), properties.getOpenAmIssoRpPassword()), serviceUserCredentials
        );
    }

    @Bean
    public NaisSystemUserTokenProvider naisSystemUserTokenProvider(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return new NaisSystemUserTokenProvider(properties.getStsDiscoveryUrl(), serviceUserCredentials.username, serviceUserCredentials.password);
    }

    @Bean
    public AktorregisterClient aktorregisterClient(EnvironmentProperties properties, NaisSystemUserTokenProvider naisSystemUserTokenProvider) {
        AktorregisterClient aktorregisterClient = new AktorregisterHttpClient(
                properties.getAktorregisterUrl(), APPLICATION_NAME, naisSystemUserTokenProvider::getSystemUserToken
        );
        return new CachedAktorregisterClient(aktorregisterClient);
    }

    @Bean
    public RegistreringClient registreringClient(AuthService authService) {
        return new RegistreringClientImpl(authService);
    }

    @Bean
    public Pep pep(EnvironmentProperties properties) {
        Credentials serviceUserCredentials = NaisUtils.getCredentials("service_user");
        return new VeilarbPep(
                properties.getAbacUrl(), serviceUserCredentials.username,
                serviceUserCredentials.password, new SpringAuditRequestInfoSupplier()
        );
    }
}
