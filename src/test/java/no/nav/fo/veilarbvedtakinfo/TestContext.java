package no.nav.fo.veilarbvedtakinfo;
import no.nav.fasit.ServiceUser;

import static java.lang.System.setProperty;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fasit.FasitUtils.getBaseUrl;
import static no.nav.fasit.FasitUtils.getServiceUser;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.STS_URL_KEY;

public class TestContext {
    private static final String SECURITY_TOKEN_SERVICE_ALIAS = "securityTokenService";
    private static final String SERVICE_USER_ALIAS = "srvveilarbvedtakinfo";
    private static final String SERVICE_USER_USERNAME = "SRVVEILARBVEDTAKINFO_USERNAME";
    private static final String SERVICE_USER_PASSWORD = "SRVVEILARBVEDTAKINFO_PASSWORD";
    private static final String FASIT_ENVIRONMENT_NAME = "FASIT_ENVIRONMENT_NAME";
    private static final String NAIS_NAMESPACE = "NAIS_NAMESPACE";

    public static void setup() {
        setProperty(STS_URL_KEY, getBaseUrl(SECURITY_TOKEN_SERVICE_ALIAS, FSS));
        ServiceUser serviceUser = getServiceUser(SERVICE_USER_ALIAS, APPLICATION_NAME);
        setProperty(SERVICE_USER_USERNAME, serviceUser.getUsername());
        setProperty(SERVICE_USER_PASSWORD, serviceUser.getPassword());
        setProperty(FASIT_ENVIRONMENT_NAME, "Q0");
        setProperty(NAIS_NAMESPACE, "Q0");


    }
}