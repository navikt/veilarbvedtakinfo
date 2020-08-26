package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;

@RestController
@RequestMapping("/behovsvurdering")
@Produces("application/json")
@Api(value = "BehovsvurderingResource", description = "Tjenester for deling behovsvurdering besvarelse")

public class BehovsvurderingResource {
    private final BehovsvurderingService bvService;
    private final UserService userService;
    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;

    public BehovsvurderingResource(
            BehovsvurderingService bvService,
            UserService userService,
            AktorregisterClient aktorregisterClient,
            Pep pep) {

        this.bvService = bvService;
        this.userService = userService;
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
    }

    @PostMapping("/svar")
    @ApiOperation(value = "Sender inn en behovsvurderings besvarelse")
    public Besvarelse nyttSvar(Svar svar) {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        userService.sjekkLeseTilgangTilPerson(aktorId);

        return bvService.nyBesvarlse(aktorId, svar);
    }

    @GetMapping("/besvarelse")
    @ApiOperation(value = "Henter den siste behovsvurderings besvarelsen på bruker")
    public Besvarelse hentSisteBesvarelse() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        userService.sjekkLeseTilgangTilPerson(aktorId);

        return bvService.hentBesvarelse(aktorId);

    }
}
