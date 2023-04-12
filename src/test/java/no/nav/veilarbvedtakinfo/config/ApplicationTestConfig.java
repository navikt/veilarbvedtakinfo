package no.nav.veilarbvedtakinfo.config;

import no.nav.common.audit_log.log.AuditLogger;
import no.nav.common.audit_log.log.AuditLoggerImpl;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.poao_tilgang.client.PoaoTilgangClient;
import no.nav.veilarbvedtakinfo.service.UnleashService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
@Import({
        ClientTestConfig.class,
        ServiceTestConfig.class,
        FilterTestConfig.class,
        ControllerTestConfig.class,
        RepositoryTestConfig.class,
        HealthConfig.class,
        DatabaseTestConfig.class,
        UnleashService.class
})
public class ApplicationTestConfig {

    @Bean
    public AuthContextHolder authContextHolder() {
        return AuthContextHolderThreadLocal.instance();
    }

	@Bean
	public PoaoTilgangClient poaoTilgangClient() { return mock(PoaoTilgangClient.class); }

	@Bean
	public UnleashClient unleashClient() { return mock(UnleashClient.class); }

	@Bean
	public AuditLogger auditLogger() { return mock(AuditLoggerImpl.class); }
}
