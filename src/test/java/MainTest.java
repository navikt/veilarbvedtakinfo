import org.springframework.boot.builder.SpringApplicationBuilder;
import static no.nav.common.utils.EnvironmentUtils.NAIS_APP_NAME_PROPERTY_NAME;
import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;

public class MainTest {

    private static final String PORT = "8800";

    public static void main(String[] args) {

        String[] arguments = {PORT};

        System.setProperty(NAIS_APP_NAME_PROPERTY_NAME, APPLICATION_NAME);

        new SpringApplicationBuilder(MainTest.class).run(arguments);
    }
}
