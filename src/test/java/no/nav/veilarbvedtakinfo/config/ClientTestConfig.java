package no.nav.veilarbvedtakinfo.config;

import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.client.aktoroppslag.BrukerIdenter;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.*;
import no.nav.poao_tilgang.client.PoaoTilgangClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class ClientTestConfig {

    @Bean
    public AktorOppslagClient aktorOppslagClient() {
        return new AktorOppslagClient() {
            @Override
            public Fnr hentFnr(AktorId aktorId) {
                return null;
            }

            @Override
            public AktorId hentAktorId(Fnr fnr) {
                return AktorId.of("TEST");
            }

            @Override
            public Map<AktorId, Fnr> hentFnrBolk(List<AktorId> aktorIdListe) {
                return null;
            }

            @Override
            public Map<Fnr, AktorId> hentAktorIdBolk(List<Fnr> fnrListe) {
                return null;
            }

            @Override
            public BrukerIdenter hentIdenter(EksternBrukerId brukerId) {
                return null;
            }

            @Override
            public HealthCheckResult checkHealth() {
                return HealthCheckResult.healthy();
            }
        };
    }

    @Bean
    public PoaoTilgangClient poaoTilgangClient() { return mock(PoaoTilgangClient.class); }
}
