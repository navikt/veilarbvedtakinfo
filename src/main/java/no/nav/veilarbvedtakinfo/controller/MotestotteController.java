package no.nav.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/motestotte")
@Api(value = "MotestotteController", description = "Tjenester for deling motestotte")
public class MotestotteController {

    private final MotestotteRepository msRepo;

    private final AuthService authService;

    @PostMapping
    @ApiOperation(value = "Sender inn en motestotte besvarelse")
    public ResponseEntity nyttSvar(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        msRepo.oppdaterMotestotte(aktorId);

        return ResponseEntity.status(204).build();
    }

    @GetMapping
    @ApiOperation(value = "Henter den siste motestotte på bruker")
    public Motestotte hent(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        Motestotte motestotte = msRepo.hentMoteStotte(aktorId);

        if (motestotte == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return motestotte;
    }
}
