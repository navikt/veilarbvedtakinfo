package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/situasjon")
@Api(value = "ArbeidsSituasjonController")
public class ArbeidsSituasjonController {

    private final AuthService authService;
    private final ArbeidSitasjonService service;

    public ArbeidsSituasjonController(AuthService authService, ArbeidSitasjonService service) {
        this.authService = authService;
        this.service = service;
    }

    @PostMapping
    public void besvarelse(@RequestBody ArbeidSituasjonSvar svar, @RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        service.nytSvar(svar, aktorId, authService.hentInnloggetSubject(), authService.erEksternBruker());
    }

    @GetMapping
    public ArbeidSituasjon hentBesvarelse(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return service.fetchSvar(aktorId);
    }

}
