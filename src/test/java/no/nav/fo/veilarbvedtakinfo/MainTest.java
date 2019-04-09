package no.nav.fo.veilarbvedtakinfo;
import no.nav.testconfig.ApiAppTest;

import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;

public class MainTest {
    private static final String PORT = "8800";

    public static void main(String[] args) throws Exception {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName(APPLICATION_NAME)
                .build()
        );

        Main.main(PORT);
    }
}