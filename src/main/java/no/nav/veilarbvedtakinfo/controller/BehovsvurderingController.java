package no.nav.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.BesvarelseDto;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/behovsvurdering")
@Api(value = "BehovsvurderingController", description = "Tjenester for deling behovsvurdering besvarelse")
public class BehovsvurderingController {

    private final BehovsvurderingService bvService;

    private final AuthService authService;

    @PostMapping("/svar")
    @ApiOperation(value = "Sender inn en behovsvurderings besvarelse")
    public BesvarelseDto nyttSvar(@RequestBody Svar svar, @RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return BesvarelseDto.fromBesvarelse(bvService.nyBesvarlse(aktorId, svar));
    }

    @GetMapping("/besvarelse")
    @ApiOperation(value = "Henter den siste behovsvurderings besvarelsen på bruker")
    public BesvarelseDto hentSisteBesvarelse(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        Besvarelse besvarelse = bvService.hentBesvarelse(aktorId);
        if (besvarelse == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return BesvarelseDto.fromBesvarelse(besvarelse);

    }
}
