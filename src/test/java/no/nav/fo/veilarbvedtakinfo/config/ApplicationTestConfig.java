package no.nav.fo.veilarbvedtakinfo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
@Import({
        SwaggerConfig.class,
        ClientTestConfig.class,
        ServiceTestConfig.class,
        FilterTestConfig.class,
        ControllerTestConfig.class,
        RepositoryTestConfig.class,
        HealthConfig.class,
        DatabaseTestConfig.class
})
public class ApplicationTestConfig {

}
