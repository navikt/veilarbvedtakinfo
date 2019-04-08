package no.nav.fo.veilarbvedtakinfo;

import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationTestConfig;
import no.nav.testconfig.ApiAppTest;

public class MainTest {

    public static void main(String[] args) throws Exception {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName("veilarbvedtakinfo")
                .build()
        );

        ApiApp.runApp(ApplicationTestConfig.class, args);
    }
}