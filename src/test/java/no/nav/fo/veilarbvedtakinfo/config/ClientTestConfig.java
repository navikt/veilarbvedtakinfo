package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.client.aktorregister.IdentOppslag;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.*;
import no.nav.fo.veilarbvedtakinfo.client.RegistreringClient;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class ClientTestConfig {

    @Bean
    public AktorregisterClient aktorregisterClient() {
        return new AktorregisterClient() {
            @Override
            public Fnr hentFnr(AktorId aktorId) {
                return null;
            }

            @Override
            public AktorId hentAktorId(Fnr fnr) {
                return AktorId.of("TEST");
            }

            @Override
            public List<IdentOppslag> hentFnr(List<AktorId> list) {
                return null;
            }

            @Override
            public List<IdentOppslag> hentAktorId(List<Fnr> list) {
                return null;
            }

            @Override
            public List<AktorId> hentAktorIder(Fnr fnr) {
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

    @Bean
    public RegistreringClient registreringClient() {
        return new RegistreringClient() {

            @Override
            public BrukerRegistreringWrapper hentSisteRegistrering(Fnr fnr) {
                return null;
            }

            @Override
            public HealthCheckResult checkHealth() {
                return null;
            }

        };
    }
}
