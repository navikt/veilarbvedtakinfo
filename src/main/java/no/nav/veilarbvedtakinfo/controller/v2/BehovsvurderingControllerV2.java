package no.nav.veilarbvedtakinfo.controller.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.BesvarelseDto;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.HentSisteBesvarelseRequest;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.NyttSvarRequest;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/behovsvurdering")
@Tag(name = "BehovsvurderingController", description = "Tjenester for deling behovsvurdering besvarelse")
public class BehovsvurderingControllerV2 {

    private final BehovsvurderingService bvService;

    private final AuthService authService;

    @PostMapping("/besvarelse")
    @Operation(summary = "Sender inn en behovsvurderings besvarelse")
    public BesvarelseDto nyttSvar(@RequestBody NyttSvarRequest nyttSvarRequest) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, nyttSvarRequest.fnr());
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(brukerFnr);

        return BesvarelseDto.fromBesvarelse(bvService.nyBesvarlse(aktorId, nyttSvarRequest.svar()));
    }

    @PostMapping("/hent-besvarelse")
    @Operation(summary = "Henter den siste behovsvurderings besvarelsen på bruker")
    public BesvarelseDto hentSisteBesvarelse(@RequestBody HentSisteBesvarelseRequest hentSisteBesvarelseRequest) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, hentSisteBesvarelseRequest.fnr());
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(brukerFnr);

        Besvarelse besvarelse = bvService.hentBesvarelse(aktorId);
        if (besvarelse == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return BesvarelseDto.fromBesvarelse(besvarelse);

    }

}
