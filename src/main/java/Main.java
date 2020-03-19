import no.nav.apiapp.ApiApp;
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
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class Main {

    public static void main(String... args) {

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

        // APP
        setProperty(AKTOER_ENDPOINT_URL, getRequiredProperty(AKTOER_V2_URL_PROPERTY_NAME));

        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
