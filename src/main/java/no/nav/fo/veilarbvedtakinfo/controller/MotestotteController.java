package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motestotte")
@Api(value = "MotestotteController", description = "Tjenester for deling motestotte")
public class MotestotteController {

    private final MotestotteRepository msRepo;
    private final AuthService authService;

    public MotestotteController(
            MotestotteRepository msRepo,
            AuthService authService) {

        this.msRepo = msRepo;
        this.authService = authService;
    }

    @PostMapping("/")
    @ApiOperation(value = "Sender inn en motestotte besvarelse")
    public void nyttSvar(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        msRepo.oppdaterMotestotte(aktorId);
    }

    @GetMapping("/")
    @ApiOperation(value = "Henter den siste motestotte på bruker")
    public Motestotte hent(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return msRepo.hentMoteStotte(aktorId);

    }
}
