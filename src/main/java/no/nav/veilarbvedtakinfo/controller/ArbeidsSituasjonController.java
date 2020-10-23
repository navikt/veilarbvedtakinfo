package no.nav.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/situasjon")
@Api(value = "ArbeidsSituasjonController")
public class ArbeidsSituasjonController {

    private final AuthService authService;

    private final ArbeidSitasjonService service;

    @PostMapping
    public ResponseEntity besvarelse(@RequestBody ArbeidSituasjonSvar svar, @RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        service.nytSvar(svar, aktorId, authService.hentInnloggetSubject(), authService.erEksternBruker());

        return ResponseEntity.status(204).build();
    }

    @GetMapping
    public ArbeidSituasjon hentBesvarelse(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        ArbeidSituasjon arbeidSituasjon = service.fetchSvar(aktorId);

        if (arbeidSituasjon == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return arbeidSituasjon;
    }

}
