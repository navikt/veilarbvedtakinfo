package no.nav.veilarbvedtakinfo;

import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VeilarbVedtakInfoApp {
    public static void main(String... args) {

        SslUtils.setupTruststore();
        SpringApplication.run(VeilarbVedtakInfoApp.class, args);
    }
}
