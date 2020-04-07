package no.nav.fo.veilarbvedtakinfo.resources;

import io.swagger.annotations.Api;
import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.service.ArbeidSitasjonService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/situasjon")
@Produces("application/json")
@Api(value = "ArbeidsSituasjonResource")
public class ArbeidsSituasjonResource {
    private final UserService userService;
    private final AktorService aktorService;
    private final PepClient pepClient;
    private final ArbeidSitasjonService service;

    public ArbeidsSituasjonResource(ArbeidSitasjonService service, UserService userService, AktorService aktorService, PepClient pepClient) {
        this.userService = userService;
        this.aktorService = aktorService;
        this.pepClient = pepClient;
        this.service = service;
    }


    @POST
    public void besvarelse(ArbeidSituasjonSvar svar) {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);
        boolean erEksternBruker = userService.erEksternBruker();

        pepClient.sjekkLesetilgangTilAktorId(aktorId.getAktorId());

        service.nytSvar(svar, aktorId, erEksternBruker);
    }

    @GET
    public ArbeidSituasjon hentBesvarelse() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);

        pepClient.sjekkLesetilgangTilAktorId(aktorId.getAktorId());

        return service.fetchSvar(aktorId);
    }

}
