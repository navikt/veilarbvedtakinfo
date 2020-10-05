package no.nav.fo.veilarbvedtakinfo.utils;

import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;

public class FnrUtils {

    public static Fnr hentFnrFraUrlEllerToken(AuthService authService, Fnr queryParamFnr) {
       return authService.erEksternBruker()
                ? Fnr.of(authService.hentInnloggetSubject())
                : queryParamFnr;
    }

}
