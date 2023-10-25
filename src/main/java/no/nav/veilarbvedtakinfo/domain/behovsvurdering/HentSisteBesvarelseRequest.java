package no.nav.veilarbvedtakinfo.domain.behovsvurdering;

import no.nav.common.types.identer.Fnr;

public record HentSisteBesvarelseRequest(
        Fnr fnr
) {
}
