package no.nav.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPepFactory;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.client.aktoroppslag.CachedAktorOppslagClient;
import no.nav.common.client.aktoroppslag.PdlAktorOppslagClient;
import no.nav.common.client.pdl.PdlClientImpl;
import no.nav.common.token_client.builder.AzureAdTokenClientBuilder;
import no.nav.common.token_client.client.AzureAdMachineToMachineTokenClient;
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

    @Bean
    public AzureAdMachineToMachineTokenClient azureAdMachineToMachineTokenClient() {
        return AzureAdTokenClientBuilder.builder()
                .withNaisDefaults()
                .buildMachineToMachineTokenClient();
    }
    @Bean
    public CachedAktorOppslagClient aktoroppslacClient(AzureAdMachineToMachineTokenClient tokenClient) {
        String url = isProduction()
                ? createProdInternalIngressUrl("pdl-api")
                : createDevInternalIngressUrl("pdl-api");

        PdlClientImpl pdlClient = new PdlClientImpl(
                url,
                () -> tokenClient.createMachineToMachineToken(String.format("api://%s.pdl.pdl-api/.default",
                        isProduction() ? "prod-fss" : "dev-fss")
                ),
                "B642"); //Samme behandlingsnummer som vedtaksstøtte

        return new CachedAktorOppslagClient(new PdlAktorOppslagClient(pdlClient));
    }

    @Bean
    public Pep pep(EnvironmentProperties properties) {
        Credentials serviceUserCredentials = getCredentials("service_user");
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
