package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.db.LocalH2Database;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.mock.*;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Configuration
public class ApplicationTestConfig extends ApplicationConfig {
    public static final boolean RUN_WITH_MOCKS = true;

    @Bean
    @Conditional(Mock.class)
    public UserService userService(ObjectProvider<HttpServletRequest> requestProvider, Pep pep){
        return new UserServiceMock(requestProvider, pep);
    }

    @Bean
    @Conditional(Mock.class)
    public RegistreringClient registreringClient(){
        return new RegistreringClientMock();
    }

    @Bean
    @Conditional(Mock.class)
    public AktorregisterClient aktorService(){
        return new AktorregisterClientMock();
    }

    @Bean
    @Conditional(Mock.class)
    public Pep pepClient(){
        return new PepClientMock();
    }
}
