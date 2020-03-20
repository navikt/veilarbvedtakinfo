package no.nav.fo.veilarbvedtakinfo;

import no.nav.brukerdialog.security.Constants;
import no.nav.fasit.FasitUtils;
import no.nav.fasit.ServiceUser;
import no.nav.sbl.dialogarena.common.abac.pep.CredentialConstants;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.EXTERNAL_USERS_AZUREAD_B2C_DISCOVERY_URL;
import static no.nav.brukerdialog.security.oidc.provider.AzureADB2CConfig.EXTERNAL_USERS_AZUREAD_B2C_EXPECTED_AUDIENCE;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fasit.FasitUtils.*;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.*;
import static no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient.VEILARBREGISTRERING_URL_PROPERTY_NAME;
import static no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.setProperty;

public class TestContext {

    private static final String SERVICE_USER_ALIAS = "srvveilarbvedtakinfo";
    private static final String SECURITY_TOKEN_SERVICE_ALIAS = "securityTokenService";
    private static final String AKTOER_V2_ALIAS = "Aktoer_v2";
    private static final String AAD_B2C_CLIENTID_ALIAS = "aad_b2c_clientid";
    private static final String AZURE_AD_B2C_DISCOVERY_ALIAS = "aad_b2c_discovery";
    private static final String ABAC_PDP_ENDPOINT_ALIAS = "abac.pdp.endpoint";

    public static void setup() {
        ServiceUser serviceUser = getServiceUser(SERVICE_USER_ALIAS, APPLICATION_NAME);

        setProperty(StsSecurityConstants.STS_URL_KEY, getBaseUrl(SECURITY_TOKEN_SERVICE_ALIAS, FSS), PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUser.getUsername(), PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.getPassword(), PUBLIC);

        setProperty(CredentialConstants.SYSTEMUSER_USERNAME, serviceUser.getUsername());
        setProperty(CredentialConstants.SYSTEMUSER_PASSWORD, serviceUser.getPassword());

        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, getRestService(ABAC_PDP_ENDPOINT_ALIAS, getDefaultEnvironment()).getUrl());
        setProperty(AKTOER_ENDPOINT_URL, getWebServiceEndpoint(AKTOER_V2_ALIAS).getUrl(), PUBLIC);

        ServiceUser issoRpUser = getServiceUser("isso-rp-user", APPLICATION_NAME);
        String issoHost = FasitUtils.getBaseUrl("isso-host");
        String issoJWS = FasitUtils.getBaseUrl("isso-jwks");
        String issoIssuer = FasitUtils.getBaseUrl("isso-issuer");
        String issoIsAlive = FasitUtils.getBaseUrl("isso.isalive", FSS);
        String loginUrl = getRestService("veilarblogin.redirect-url", getDefaultEnvironment(), "fss").getUrl();

        setProperty(Constants.ISSO_HOST_URL_PROPERTY_NAME, issoHost);
        setProperty(Constants.ISSO_RP_USER_USERNAME_PROPERTY_NAME, issoRpUser.getUsername());
        setProperty(Constants.ISSO_RP_USER_PASSWORD_PROPERTY_NAME, issoRpUser.getPassword());
        setProperty(Constants.ISSO_JWKS_URL_PROPERTY_NAME, issoJWS);
        setProperty(Constants.ISSO_ISSUER_URL_PROPERTY_NAME, issoIssuer);
        setProperty(Constants.ISSO_ISALIVE_URL_PROPERTY_NAME, issoIsAlive);
        setProperty(Constants.OIDC_REDIRECT_URL_PROPERTY_NAME, loginUrl);

        ServiceUser aadB2cUser = getServiceUser(AAD_B2C_CLIENTID_ALIAS, APPLICATION_NAME);
        setProperty(EXTERNAL_USERS_AZUREAD_B2C_DISCOVERY_URL, getBaseUrl(AZURE_AD_B2C_DISCOVERY_ALIAS), PUBLIC);
        setProperty(EXTERNAL_USERS_AZUREAD_B2C_EXPECTED_AUDIENCE, aadB2cUser.getUsername(), PUBLIC);

        setProperty(VEILARBREGISTRERING_URL_PROPERTY_NAME, "http://localhost:8080");
    }
}
