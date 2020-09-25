package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motestotte")
@Api(value = "MotestotteController", description = "Tjenester for deling motestotte")
public class MotestotteController {
    private final MotestotteRepository msRepo;
    private final UserService userService;
    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;

    public MotestotteController(
            MotestotteRepository msRepo,
            UserService userService,
            AktorregisterClient aktorregisterClient,
            Pep pep) {

        this.msRepo = msRepo;
        this.userService = userService;
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
    }

    @PostMapping("/")
    @ApiOperation(value = "Sender inn en motestotte besvarelse")
    public void nyttSvar() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        userService.sjekkLeseTilgangTilPerson(aktorId);
        msRepo.oppdaterMotestotte(aktorId);
    }

    @GetMapping("/")
    @ApiOperation(value = "Henter den siste motestotte på bruker")
    public Motestotte hent() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return msRepo.hentMoteStotte(aktorId);

    }
}
