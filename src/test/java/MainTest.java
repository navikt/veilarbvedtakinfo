import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbvedtakinfo.TestContext;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationTestConfig;
import no.nav.fo.veilarbvedtakinfo.db.DbTestUtils;
import no.nav.testconfig.ApiAppTest;

import static java.lang.System.getProperty;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;
import static no.nav.testconfig.ApiAppTest.Config.builder;

public class MainTest {
    private static final String PORT = "8800";

    public static void main(String[] args) {
        String[] arguments = {PORT};

        ApiAppTest.setupTestContext(builder().applicationName(APPLICATION_NAME).build());
        TestContext.setup();
        DbTestUtils.setupContext(getProperty("database"));

        ApiApp.runApp(ApplicationTestConfig.class, arguments);
    }
}
