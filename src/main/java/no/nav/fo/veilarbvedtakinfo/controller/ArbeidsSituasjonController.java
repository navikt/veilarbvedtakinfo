package no.nav.fo.veilarbvedtakinfo.controller;

import io.swagger.annotations.Api;
import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/situasjon")
@Api(value = "ArbeidsSituasjonController")
public class ArbeidsSituasjonController {
    private final UserService userService;
    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;
    private final ArbeidSitasjonService service;

    public ArbeidsSituasjonController(ArbeidSitasjonService service, UserService userService, AktorregisterClient aktorregisterClient, Pep pep) {
        this.userService = userService;
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
        this.service = service;
    }

    @PostMapping
    public void besvarelse(ArbeidSituasjonSvar svar) {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        userService.sjekkLeseTilgangTilPerson(aktorId);
        boolean erEksternBruker = userService.erEksternBruker();
        String avsenderID = userService.getUid();
        service.nytSvar(svar, aktorId, avsenderID, erEksternBruker);
    }

    @GetMapping
    public ArbeidSituasjon hentBesvarelse() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        String aktorId = aktorregisterClient.hentAktorId(fnr);
        userService.sjekkLeseTilgangTilPerson(aktorId);
        return service.fetchSvar(aktorId);
    }

}
