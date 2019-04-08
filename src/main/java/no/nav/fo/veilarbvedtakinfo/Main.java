package no.nav.fo.veilarbvedtakinfo;
import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig;
import static java.lang.System.setProperty;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;

public class Main {

    public static void main(String... args) throws Exception {
        setProperty("applicationName", APPLICATION_NAME);

        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
