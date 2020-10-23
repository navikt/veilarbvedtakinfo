package no.nav.veilarbvedtakinfo.client;

import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;

public interface RegistreringClient extends HealthCheck {

    BrukerRegistreringWrapper hentSisteRegistrering(Fnr fnr);
}
