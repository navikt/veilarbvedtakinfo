package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.auth.context.UserRole;
import no.nav.fo.veilarbvedtakinfo.test.TestAuthContextFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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
        HealthConfig.class
})
public class ApplicationTestConfig {

    @Bean
    public FilterRegistrationBean testSubjectFilterRegistrationBean() {
        FilterRegistrationBean<TestAuthContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestAuthContextFilter(UserRole.INTERN, "randomUID"));
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}
