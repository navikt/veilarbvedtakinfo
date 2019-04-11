import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbvedtakinfo.TestContext;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationTestConfig;
import no.nav.fo.veilarbvedtakinfo.db.DatabaseTestContext;
import no.nav.testconfig.ApiAppTest;

import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;

public class MainTest {
    private static final String PORT = "8800";

    public static void main(String[] args) throws Exception {
        String arguments[] = {PORT};
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName(APPLICATION_NAME)
                .build()
        );
        TestContext.setup();
        DatabaseTestContext.setupContext("Q0");
        ApiApp.runApp(ApplicationTestConfig.class, arguments);
    }
}