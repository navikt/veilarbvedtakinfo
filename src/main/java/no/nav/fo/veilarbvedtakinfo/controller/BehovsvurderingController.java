package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.BesvarelseDto;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.fo.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/behovsvurdering")
@Api(value = "BehovsvurderingController", description = "Tjenester for deling behovsvurdering besvarelse")
public class BehovsvurderingController {

    private final BehovsvurderingService bvService;
    private final AuthService authService;

    public BehovsvurderingController(BehovsvurderingService bvService, AuthService authService) {
        this.bvService = bvService;
        this.authService = authService;
    }

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

        return BesvarelseDto.fromBesvarelse(bvService.hentBesvarelse(aktorId));

    }
}
