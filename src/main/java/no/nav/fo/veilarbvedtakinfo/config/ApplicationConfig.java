package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbvedtakinfo.db.MigrationUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;


@Configuration
@Import({ServiceBeans.class,
        DatabaseConfig.class,
        AktorConfig.class,
        PepConfig.class
})

public class ApplicationConfig implements ApiApplication {

    public static final String APPLICATION_NAME = "veilarbvedtakinfo";

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getContextPath() {
        return "/veilarbvedtakinfo";
    }

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }

    @Transactional
    @Override
    public void startup(ServletContext servletContext) {
        MigrationUtils.createTables(jdbcTemplate);
    }

}
