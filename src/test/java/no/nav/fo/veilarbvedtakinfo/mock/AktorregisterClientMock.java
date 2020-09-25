package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.client.aktorregister.IdentOppslag;
import no.nav.common.health.HealthCheckResult;

import java.util.List;

public class AktorregisterClientMock implements AktorregisterClient {

    @Override
    public String hentFnr(String s) {
        return s;
    }

    @Override
    public String hentAktorId(String s) {
        return s;
    }

    @Override
    public List<IdentOppslag> hentFnr(List<String> list) {
        return null;
    }

    @Override
    public List<IdentOppslag> hentAktorId(List<String> list) {
        return null;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return null;
    }
}
