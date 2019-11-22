package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;


@Configuration
@Import({
    ServiceBeans.class,
    DatabaseConfig.class,
    AktorConfig.class,
    PepConfig.class
})
public class ApplicationConfig implements ApiApplication {

    public static final String APPLICATION_NAME = "veilarbvedtakinfo";
    public static final String AKTOER_V2_URL_PROPERTY_NAME = "AKTOER_V2_ENDPOINTURL";
    public static final String SECURITYTOKENSERVICE_URL_PROPERTY_NAME = "SECURITYTOKENSERVICE_URL";

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .sts()
                .azureADB2CLogin()
                .issoLogin();
    }

    @Transactional
    @Override
    public void startup(ServletContext servletContext) {
        DatabaseUtils.createTables(jdbcTemplate);
    }

}
