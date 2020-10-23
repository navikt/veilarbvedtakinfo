package no.nav.veilarbvedtakinfo.utils;

import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FnrUtils {

    public static Fnr hentFnrFraUrlEllerToken(AuthService authService, Fnr queryParamFnr) {
       Fnr fnr = authService.erEksternBruker()
                ? Fnr.of(authService.hentInnloggetSubject())
                : queryParamFnr;

       if (fnr == null) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fnr mangler");
       }

       return fnr;
    }

}
