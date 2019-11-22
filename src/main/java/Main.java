import no.nav.apiapp.ApiApp;
import no.nav.brukerdialog.tools.SecurityConstants;
import no.nav.common.utils.NaisUtils;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig;
import no.nav.sbl.dialogarena.common.abac.pep.CredentialConstants;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;

import static java.lang.System.setProperty;
import static no.nav.common.utils.NaisUtils.getCredentials;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.*;
import static no.nav.fo.veilarbvedtakinfo.config.DatabaseConfig.VEILARBVEDTAKINFODB_PASSWORD;
import static no.nav.fo.veilarbvedtakinfo.config.DatabaseConfig.VEILARBVEDTAKINFODB_USERNAME;
import static no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient.VEILARBREGISTRERING_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static void main(String... args) {
        readFromConfigMap();

        NaisUtils.Credentials serviceUser = getCredentials("service_user");
        NaisUtils.Credentials oracleCreds = getCredentials("oracle_creds");

        // DB
        System.setProperty(VEILARBVEDTAKINFODB_USERNAME, oracleCreds.username);
        System.setProperty(VEILARBVEDTAKINFODB_PASSWORD, oracleCreds.password);

        // STS
        setProperty(StsSecurityConstants.STS_URL_KEY, getRequiredProperty(SECURITYTOKENSERVICE_URL_PROPERTY_NAME));
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUser.username);
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.password);

        // ABAC
        System.setProperty(CredentialConstants.SYSTEMUSER_USERNAME, serviceUser.username);
        System.setProperty(CredentialConstants.SYSTEMUSER_PASSWORD, serviceUser.password);

        // OIDC
        System.setProperty(SecurityConstants.SYSTEMUSER_USERNAME, serviceUser.username);
        System.setProperty(SecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.password);

        // APP
        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_URL_PROPERTY_NAME));

        ApiApp.runApp(ApplicationConfig.class, args);
    }

    private static void readFromConfigMap() {
        NaisUtils.addConfigMapToEnv("pto-config",
                AKTOER_V2_URL_PROPERTY_NAME,
                "ABAC_PDP_ENDPOINT_URL",
                "ABAC_PDP_ENDPOINT_DESCRIPTION",
                "AAD_B2C_DISCOVERY_URL",
                "AAD_B2C_CLIENTID_USERNAME",
                "ISSO_HOST_URL",
                "ISSO_JWKS_URL",
                "ISSO_ISSUER_URL",
                "ISSO_ISALIVE_URL",
                SECURITYTOKENSERVICE_URL_PROPERTY_NAME,
                "SECURITYTOKENSERVICE_URL",
                "UNLEASH_API_URL",
                "VEILARBLOGIN_REDIRECT_URL_DESCRIPTION",
                "OIDC_REDIRECT_URL",
                VEILARBREGISTRERING_URL_PROPERTY_NAME
        );
    }

}
