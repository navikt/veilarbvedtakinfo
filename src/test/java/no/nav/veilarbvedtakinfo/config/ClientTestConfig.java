package no.nav.veilarbvedtakinfo.config;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.client.aktoroppslag.BrukerIdenter;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.*;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

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
    public Pep veilarbPep() {
        return new Pep() {
            @Override
            public boolean harVeilederTilgangTilEnhet(NavIdent navIdent, EnhetId enhetId) {
                return true;
            }

            @Override
            public boolean harTilgangTilEnhet(String s, EnhetId enhetId) {
                return true;
            }

            @Override
            public boolean harTilgangTilEnhetMedSperre(String s, EnhetId enhetId) {
                return true;
            }

            @Override
            public boolean harTilgangTilEnhetMedSperre(NavIdent navIdent, EnhetId enhetId) {
                return true;
            }

            @Override
            public boolean harVeilederTilgangTilPerson(NavIdent navIdent, ActionId actionId, EksternBrukerId eksternBrukerId) {
                return true;
            }

            @Override
            public boolean harTilgangTilPerson(String s, ActionId actionId, EksternBrukerId eksternBrukerId) {
                return true;
            }

            @Override
            public boolean harTilgangTilOppfolging(String s) {
                return true;
            }

            @Override
            public boolean harVeilederTilgangTilModia(String s) {
                return true;
            }

            @Override
            public boolean harVeilederTilgangTilKode6(NavIdent navIdent) {
                return true;
            }

            @Override
            public boolean harVeilederTilgangTilKode7(NavIdent navIdent) {
                return true;
            }

            @Override
            public boolean harVeilederTilgangTilEgenAnsatt(NavIdent navIdent) {
                return true;
            }

            @Override
            public AbacClient getAbacClient() {
                return null;
            }
        };
    }
}
