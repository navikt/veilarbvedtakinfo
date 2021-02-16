package no.nav.veilarbvedtakinfo.config;

import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestChecks;
import no.nav.common.health.selftest.SelfTestMeterBinder;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static java.util.Collections.singletonList;

@Configuration
public class HealthConfig {

    @Bean
    public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource, "SELECT 1 FROM DUAL");
    }

    @Bean
    public SelfTestChecks selfTestChecks(DataSourceHealthIndicator dataSourceHealthIndicator) {

        SelfTestCheck databaseselfTestCheck =
                new SelfTestCheck("Ping database", true, () -> checkDbHealth(dataSourceHealthIndicator));

        return new SelfTestChecks(singletonList(databaseselfTestCheck));
    }

    @Bean
    public SelfTestMeterBinder selfTestMeterBinder(SelfTestChecks selfTestChecks) {
        return new SelfTestMeterBinder(selfTestChecks);
    }

    private HealthCheckResult checkDbHealth(DataSourceHealthIndicator dataSourceHealthIndicator) {
        Health health = dataSourceHealthIndicator.health();
        if (Status.UP.equals(health.getStatus())) {
            return HealthCheckResult.healthy();
        } else {
            return HealthCheckResult.unhealthy("Fikk ikke kontakt med databasen" + health.getDetails().toString());
        }
    }
}
