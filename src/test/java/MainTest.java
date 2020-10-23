import no.nav.veilarbvedtakinfo.config.ApplicationTestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import(ApplicationTestConfig.class)
public class MainTest {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MainTest.class);
        application.setAdditionalProfiles("local");
        application.run(args);
    }

}
