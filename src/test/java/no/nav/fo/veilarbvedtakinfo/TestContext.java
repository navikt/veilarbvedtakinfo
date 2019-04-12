package no.nav.fo.veilarbvedtakinfo;
import no.nav.fasit.FasitUtils;
import no.nav.fasit.ServiceUser;
import no.nav.testconfig.util.Util;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fasit.FasitUtils.getBaseUrl;
import static no.nav.fasit.FasitUtils.getServiceUser;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.STS_URL_KEY;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME;

public class TestContext {
    private static final String SECURITY_TOKEN_SERVICE_ALIAS = "securityTokenService";
    private static final String SERVICE_USER_ALIAS = "srvveilarbvedtakinfo";
    private static final String SERVICE_USER_USERNAME = "SRVVEILARBVEDTAKINFO_USERNAME";
    private static final String SERVICE_USER_PASSWORD = "SRVVEILARBVEDTAKINFO_PASSWORD";
    private static final String FASIT_ENVIRONMENT_NAME = "FASIT_ENVIRONMENT_NAME";
    private static final String NAIS_NAMESPACE = "NAIS_NAMESPACE";
    private static final String TEST_ENV = "Q0";
    private static final String AZURE_AD_B2C_DISCOVERY_ALIAS = "aad_b2c_discovery";

    public static void setup() {

        //Service user
        setProperty(STS_URL_KEY, getBaseUrl(SECURITY_TOKEN_SERVICE_ALIAS, FSS));
        ServiceUser serviceUser = getServiceUser(SERVICE_USER_ALIAS, APPLICATION_NAME);
        setProperty(SERVICE_USER_USERNAME, serviceUser.getUsername());
        setProperty(SERVICE_USER_PASSWORD, serviceUser.getPassword());

        setProperty(FASIT_ENVIRONMENT_NAME, TEST_ENV);
        setProperty(NAIS_NAMESPACE, TEST_ENV);

        //AktorService
        setProperty(AKTOER_ENDPOINT_URL, "https://app-" + TEST_ENV + ".adeo.no/aktoerid/AktoerService/v2");

        //ABAC
        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, "https://wasapp-" + TEST_ENV + ".adeo.no/asm-pdp/authorize");

        //AZURE
        ServiceUser azureADClientId = getServiceUser("aad_b2c_clientid", APPLICATION_NAME);
        Util.setProperty(AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME, FasitUtils.getBaseUrl("aad_b2c_discovery"));
        Util.setProperty(AZUREAD_B2C_EXPECTED_AUDIENCE_PROPERTY_NAME, azureADClientId.username);
        setProperty(AZUREAD_B2C_DISCOVERY_URL_PROPERTY_NAME, getBaseUrl(AZURE_AD_B2C_DISCOVERY_ALIAS));


    }
}