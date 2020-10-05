package no.nav.fo.veilarbvedtakinfo.httpclient;

import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;

public interface RegistreringClient extends HealthCheck {

    BrukerRegistreringWrapper hentSisteRegistrering(Fnr fnr);
}
