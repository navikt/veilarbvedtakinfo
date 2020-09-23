package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.oidc.Constants;
import no.nav.common.oidc.auth.OidcAuthenticatorConfig;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import static no.nav.brukerdialog.security.Constants.*;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;


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

    private OidcAuthenticatorConfig createVeilarbloginAADConfig() {
        String discoveryUrl = getRequiredProperty("AAD_DISCOVERY_URL");
        String clientId = getRequiredProperty("VEILARBLOGIN_AAD_CLIENT_ID");

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withIdTokenCookieName(Constants.ISSO_ID_TOKEN_COOKIE_NAME)
                .withIdentType(IdentType.InternBruker);
    }

    private OidcAuthenticatorConfig createOpenAmAuthenticatorConfig() {
        String discoveryUrl = getRequiredProperty("OPENAM_DISCOVERY_URL");
        String clientId = getRequiredProperty("VEILARBLOGIN_OPENAM_CLIENT_ID");
        String refreshUrl = getRequiredProperty("VEILARBLOGIN_OPENAM_REFRESH_URL");

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withRefreshUrl(refreshUrl)
                .withRefreshTokenCookieName(REFRESH_TOKEN_COOKIE_NAME)
                .withIdTokenCookieName(ID_TOKEN_COOKIE_NAME)
                .withIdentType(IdentType.InternBruker);
    }

    private OidcAuthenticatorConfig createAzureAdB2CConfig() {
        String discoveryUrl = getRequiredProperty("AAD_B2C_DISCOVERY_URL");
        String clientId = getRequiredProperty("AAD_B2C_CLIENTID_USERNAME");

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withIdTokenCookieName(AZUREADB2C_OIDC_COOKIE_NAME_SBS)
                .withIdentType(IdentType.EksternBruker);
    }

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .addOidcAuthenticator(createOpenAmAuthenticatorConfig())
                .addOidcAuthenticator(createAzureAdB2CConfig())
                .addOidcAuthenticator(createVeilarbloginAADConfig())
                .sts();
    }

    @Transactional
    @Override
    public void startup(ServletContext servletContext) {
        DatabaseUtils.createTables(jdbcTemplate);
    }

}
