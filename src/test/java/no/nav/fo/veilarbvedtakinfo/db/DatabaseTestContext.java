package no.nav.fo.veilarbvedtakinfo.db;

import lombok.val;
import no.nav.fasit.DbCredentials;
import no.nav.fasit.FasitUtils;
import no.nav.fasit.TestEnvironment;

import java.util.Optional;

import static no.nav.fo.veilarbvedtakinfo.config.ApplicationConfig.APPLICATION_NAME;
import static no.nav.fo.veilarbvedtakinfo.config.DatabaseConfig.*;

public class DatabaseTestContext {

    public static void setupContext(String miljo) {
        val dbCredential = Optional.ofNullable(miljo)
                .map(TestEnvironment::valueOf)
                .map(testEnvironment -> FasitUtils.getDbCredentials(testEnvironment, APPLICATION_NAME));

        if (dbCredential.isPresent()) {
            setDataSourceProperties(dbCredential.get());
        } else {
            setInMemoryDataSourceProperties();
        }
    }

    public static void setupInMemoryContext() {
        setupContext(null);
    }

    private static void setDataSourceProperties(DbCredentials dbCredentials) {
        System.setProperty(VEILARBVEDTAKINFODB_URL, dbCredentials.url);
        System.setProperty(VEILARBVEDTAKINFODB_USERNAME, dbCredentials.getUsername());
        System.setProperty(VEILARBVEDTAKINFODB_PASSWORD, dbCredentials.getPassword());

    }

    private static void setInMemoryDataSourceProperties() {
        System.setProperty(VEILARBVEDTAKINFODB_URL,
                "jdbc:h2:mem:veilarbvedtakinfo;DB_CLOSE_DELAY=-1;MODE=Oracle");
        System.setProperty(VEILARBVEDTAKINFODB_USERNAME, "sa");
        System.setProperty(VEILARBVEDTAKINFODB_PASSWORD, "password");
    }
}