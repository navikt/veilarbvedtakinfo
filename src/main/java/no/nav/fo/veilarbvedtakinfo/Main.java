package no.nav.fo.veilarbvedtakinfo;
import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig;

public class Main {

    public static void main(String... args) throws Exception {
        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
