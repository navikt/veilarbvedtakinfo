package no.nav.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPepFactory;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.client.aktoroppslag.CachedAktorOppslagClient;
import no.nav.common.client.aktoroppslag.PdlAktorOppslagClient;
import no.nav.common.client.pdl.PdlClientImpl;
import no.nav.common.sts.NaisSystemUserTokenProvider;
import no.nav.common.sts.OpenAmSystemUserTokenProvider;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.EnvironmentUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.NaisUtils.getCredentials;
import static no.nav.common.utils.UrlUtils.createDevInternalIngressUrl;
import static no.nav.common.utils.UrlUtils.createProdInternalIngressUrl;

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
    public CachedAktorOppslagClient aktoroppslacClient(NaisSystemUserTokenProvider naisSystemUserTokenProvider) {
        String url = isProduction()
                ? createProdInternalIngressUrl("pdl-api")
                : createDevInternalIngressUrl("pdl-api-q1");

        PdlClientImpl pdlClient = new PdlClientImpl(
                url,
                naisSystemUserTokenProvider::getSystemUserToken,
                naisSystemUserTokenProvider::getSystemUserToken);

        return new CachedAktorOppslagClient(new PdlAktorOppslagClient(pdlClient));
    }

    @Bean
    public Pep pep(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return VeilarbPepFactory.get(
                properties.getAbacUrl(), serviceUserCredentials.username,
                serviceUserCredentials.password, new SpringAuditRequestInfoSupplier());
    }

    @Bean
    public AuthContextHolder authContextHolder() {
        return AuthContextHolderThreadLocal.instance();
    }

    private static boolean isProduction() {
        return EnvironmentUtils.isProduction().orElseThrow();
    }
}
