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

    /**
     * TODO
     * Denne kontrolleren er egentlig ikke i bruk.
     * GET hentBesvarelse() blir brukt av veientilarbeid, men siden det er ingen som bruker POST besvarelse(),
     * så er resultatet alltid statuskode 204. Alt relatert til arbeidssituasjon kan trolig fjernes.
     */

    private final AuthService authService;

    private final ArbeidSitasjonService arbeidSitasjonService;

    @PostMapping
    public ResponseEntity besvarelse(@RequestBody ArbeidSituasjonSvar svar, @RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        arbeidSitasjonService.opprettSvar(svar, aktorId, authService.hentInnloggetSubject(), authService.erEksternBruker());

        return ResponseEntity.status(204).build();
    }

    @GetMapping
    public ArbeidSituasjon hentBesvarelse(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        ArbeidSituasjon arbeidSituasjon = arbeidSitasjonService.fetchSvar(aktorId);

        if (arbeidSituasjon == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return arbeidSituasjon;
    }

}
