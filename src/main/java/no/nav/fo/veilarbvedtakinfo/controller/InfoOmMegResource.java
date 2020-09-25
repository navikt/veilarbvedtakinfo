package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.InfoOmMegData;
import no.nav.fo.veilarbvedtakinfo.service.InfoOmMegService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;

import java.util.List;

@RestController
@RequestMapping("/api")
@Produces("application/json")
@Api(value = "InfoOmMegResource", description = "Tjenester for deling av arbeidssøkerstatus.")
public class InfoOmMegResource {
    private final InfoOmMegService infoOmMegService;
    private final UserService userService;
    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;

    public InfoOmMegResource(InfoOmMegService infoOmMegService,
                             UserService userService,
                             AktorregisterClient aktorregisterClient,
                             Pep pep){

        this.infoOmMegService = infoOmMegService;
        this.userService = userService;
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
    }

    @GetMapping("/sistesituasjon")
    @ApiOperation(value = "Henter alle sist lagrede verdier.")
    public InfoOmMegData hentSisteSituasjon() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentSisteSituasjon(aktorId, fnr);
    }

    @GetMapping("/fremtidigsituasjon")
    @ApiOperation(value = "Henter nyeste verdi for fremtidig situasjon.")
    public HovedmalData hentFremtidigSituasjon() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentFremtidigSituasjon(aktorId, fnr);
    }

    @GetMapping("/situasjonliste")
    @ApiOperation(value = "Henter nyeste verdi for fremtidig situasjon.")
    public List<HovedmalData> hentSituasjonListe() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentSituasjonHistorikk(aktorId);
    }

    @PostMapping("/fremtidigsituasjon")
    @ApiOperation(value = "Oppdaterer fremtidig situasjon")
    public HovedmalData oppdaterFremtidigSituasjon(HovedmalData fremtidigSituasjonData) {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        String endretAv = userService.erEksternBruker() ? aktorId : userService.getUid();

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreFremtidigSituasjon(fremtidigSituasjonData, aktorId, endretAv);
    }

    @GetMapping("/helsehinder")
    @ApiOperation(value = "Henter siste lagrede verdi av helsehinder.")
    public HelseOgAndreHensynData hentHelseHinder() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentHelseHinder(aktorId, fnr);
    }

    @PostMapping("/helsehinder")
    @ApiOperation(value = "Lagrer helsehinder.")
    public HelseOgAndreHensynData lagreHelseHinder(HelseOgAndreHensynData helseOgAndreHensynData){
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreHelseHinder(helseOgAndreHensynData, aktorId);
    }

    @GetMapping("/andrehinder")
    @ApiOperation(value = "Henter siste lagrede verdi av andre hinder.")
    public HelseOgAndreHensynData hentAndreHinder() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.hentAndreHinder(aktorId, fnr);
    }

    @PostMapping("/andrehinder")
    @ApiOperation(value = "Lagrer andre hinder.")
    public HelseOgAndreHensynData lagreAndreHinder(HelseOgAndreHensynData helseOgAndreHensynData){
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);

        userService.sjekkLeseTilgangTilPerson(aktorId);

        return infoOmMegService.lagreAndreHinder(helseOgAndreHensynData, aktorId);
    }

}
