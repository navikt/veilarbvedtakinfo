package no.nav.veilarbvedtakinfo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.env")
public class EnvironmentProperties {
    private String abacUrl;
    private String aktorregisterUrl;
    private String stsDiscoveryUrl;
    private String dbUrl;
    private String openAmDiscoveryUrl;
    private String openAmRedirectUrl;
    private String openAmClientId;
    private String openAmRefreshUrl;
    private String aadB2cDiscoveryUrl;
    private String aadB2cClientId;
    private String openAmIssoRpUsername;
    private String openAmIssoRpPassword;
    private String aadDiscoveryUrl;
    private String veilarbloginAadClientId;
}
