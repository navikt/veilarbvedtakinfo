package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.InfoOmMegData;
import no.nav.fo.veilarbvedtakinfo.service.AuthService;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.utils.FnrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "InfoOmMegController", description = "Tjenester for deling av arbeidssøkerstatus.")
public class InfoOmMegController {

    private final InfoOmMegService infoOmMegService;
    private final AuthService authService;

    @Autowired
    public InfoOmMegController(InfoOmMegService infoOmMegService, AuthService authService){
        this.infoOmMegService = infoOmMegService;
        this.authService = authService;
    }

    @GetMapping("/sistesituasjon")
    @ApiOperation(value = "Henter alle sist lagrede verdier.")
    public InfoOmMegData hentSisteSituasjon(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentSisteSituasjon(aktorId, fnr);
    }

    @GetMapping("/fremtidigsituasjon")
    @ApiOperation(value = "Henter nyeste verdi for fremtidig situasjon.")
    public HovedmalData hentFremtidigSituasjon(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        HovedmalData hovedmalData = infoOmMegService.hentFremtidigSituasjon(aktorId, fnr);
        if (hovedmalData == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return hovedmalData;
    }

    @GetMapping("/situasjonliste")
    @ApiOperation(value = "Henter nyeste verdi for fremtidig situasjon.")
    public List<HovedmalData> hentSituasjonListe(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentSituasjonHistorikk(aktorId);
    }

    @PostMapping("/fremtidigsituasjon")
    @ApiOperation(value = "Oppdaterer fremtidig situasjon")
    public HovedmalData oppdaterFremtidigSituasjon(@RequestBody HovedmalData fremtidigSituasjonData, @RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        String endretAv = authService.erEksternBruker() ? aktorId.get() : authService.hentInnloggetSubject();

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreFremtidigSituasjon(fremtidigSituasjonData, aktorId, endretAv);
    }

    @GetMapping("/helsehinder")
    @ApiOperation(value = "Henter siste lagrede verdi av helsehinder.")
    public HelseOgAndreHensynData hentHelseHinder(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        HelseOgAndreHensynData helseOgAndreHensynData = infoOmMegService.hentHelseHinder(aktorId, fnr);
        if (helseOgAndreHensynData == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return helseOgAndreHensynData;
    }

    @PostMapping("/helsehinder")
    @ApiOperation(value = "Lagrer helsehinder.")
    public HelseOgAndreHensynData lagreHelseHinder(@RequestBody HelseOgAndreHensynData helseOgAndreHensynData, @RequestParam(required = false, name = "fnr") Fnr fnr){
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreHelseHinder(helseOgAndreHensynData, aktorId);
    }

    @GetMapping("/andrehinder")
    @ApiOperation(value = "Henter siste lagrede verdi av andre hinder.")
    public HelseOgAndreHensynData hentAndreHinder(@RequestParam(required = false, name = "fnr") Fnr fnr) {
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        HelseOgAndreHensynData helseOgAndreHensynData = infoOmMegService.hentAndreHinder(aktorId, fnr);
        if (helseOgAndreHensynData == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return helseOgAndreHensynData;
    }

    @PostMapping("/andrehinder")
    @ApiOperation(value = "Lagrer andre hinder.")
    public HelseOgAndreHensynData lagreAndreHinder(@RequestBody HelseOgAndreHensynData helseOgAndreHensynData, @RequestParam(required = false, name = "fnr") Fnr fnr){
        Fnr brukerFnr = FnrUtils.hentFnrFraUrlEllerToken(authService, fnr);
        AktorId aktorId = authService.hentAktorId(brukerFnr);

        authService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreAndreHinder(helseOgAndreHensynData, aktorId);
    }

}
