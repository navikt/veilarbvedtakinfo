package no.nav.veilarbvedtakinfo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "MotestotteController", description = "Tjenester for deling motestotte")
public class MotestotteController {

    private final MotestotteRepository msRepo;

    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Sender inn en motestotte besvarelse")
    public ResponseEntity nyttSvar(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(brukerFnr);

        msRepo.oppdaterMotestotte(aktorId);

        return ResponseEntity.status(204).build();
    }

    @GetMapping
    @Operation(summary = "Henter den siste motestotte på bruker")
    public Motestotte hent(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(brukerFnr);

        Motestotte motestotte = msRepo.hentMoteStotte(aktorId);

        if (motestotte == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return motestotte;
    }
}
