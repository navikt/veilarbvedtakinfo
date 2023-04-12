package no.nav.veilarbvedtakinfo.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPepFactory;
import no.nav.common.audit_log.log.AuditLogger;
import no.nav.common.audit_log.log.AuditLoggerImpl;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.client.aktoroppslag.CachedAktorOppslagClient;
import no.nav.common.client.aktoroppslag.PdlAktorOppslagClient;
import no.nav.common.client.pdl.PdlClientImpl;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.featuretoggle.UnleashClientImpl;
import no.nav.common.rest.client.RestClient;
import no.nav.common.token_client.builder.AzureAdTokenClientBuilder;
import no.nav.common.token_client.client.AzureAdMachineToMachineTokenClient;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.poao_tilgang.client.AdGruppe;
import no.nav.poao_tilgang.client.Decision;
import no.nav.poao_tilgang.client.PoaoTilgangCachedClient;
import no.nav.poao_tilgang.client.PoaoTilgangClient;
import no.nav.poao_tilgang.client.PoaoTilgangHttpClient;
import no.nav.poao_tilgang.client.PolicyInput;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static no.nav.common.utils.NaisUtils.getCredentials;
import static no.nav.common.utils.UrlUtils.createDevInternalIngressUrl;
import static no.nav.common.utils.UrlUtils.createProdInternalIngressUrl;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
public class ApplicationConfig {
	public static final String APPLICATION_NAME = "veilarbvedtakinfo";

	private final Cache<PolicyInput, Decision> policyInputToDecisionCache = Caffeine.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(30))
			.build();
	private final Cache<UUID, List<AdGruppe>> navAnsattIdToAzureAdGrupperCache = Caffeine.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(30))
			.build();
	private final Cache<String, Boolean> norskIdentToErSkjermetCache = Caffeine.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(30))
			.build();
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
                ));

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

	@Bean
	public UnleashClient unleashClient(EnvironmentProperties properties) {
		return new UnleashClientImpl(properties.getUnleashUrl(), APPLICATION_NAME);
	}

	@Bean
	public PoaoTilgangClient poaoTilgangClient(EnvironmentProperties properties, AzureAdMachineToMachineTokenClient tokenClient) {
		return new PoaoTilgangCachedClient(
				new PoaoTilgangHttpClient(
						properties.getPoaoTilgangUrl(),
						() -> tokenClient.createMachineToMachineToken(properties.getPoaoTilgangScope()),
						RestClient.baseClient()
				),
				policyInputToDecisionCache,
				navAnsattIdToAzureAdGrupperCache,
				norskIdentToErSkjermetCache
		);
	}

	@Bean
	AuditLogger auditLogger() {
		return new AuditLoggerImpl();
	}
    private static boolean isProduction() {
        return EnvironmentUtils.isProduction().orElseThrow();
    }
}
