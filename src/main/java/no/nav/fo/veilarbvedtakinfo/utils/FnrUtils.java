package no.nav.fo.veilarbvedtakinfo.utils;

import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;

public class FnrUtils {

    public static AktorId getAktorIdOrElseThrow(AktorService aktorService, String fnr) {
        return aktorService.getAktorId(fnr)
                .map(AktorId::new)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke aktør for fnr: " + fnr));
    }
}
