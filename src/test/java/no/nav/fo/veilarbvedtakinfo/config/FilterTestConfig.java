package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.auth.context.UserRole;
import no.nav.fo.veilarbvedtakinfo.test.TestAuthContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FilterTestConfig {

    @Bean
    public FilterRegistrationBean testSubjectFilterRegistrationBean() {
        FilterRegistrationBean<TestAuthContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestAuthContextFilter(UserRole.INTERN, "randomUID"));
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }
}
