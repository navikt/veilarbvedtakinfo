package no.nav.fo.veilarbvedtakinfo.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.fo.veilarbvedtakinfo.service.BehovsvurderingService;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/behovsvurdering")
@Produces("application/json")
@Api(value = "BehovsvurderingResource", description = "Tjenester for deling behovsvurdering besvarelse")

public class BehovsvurderingResource {
    private final BehovsvurderingService bvService;
    private final UserService userService;
    private final AktorService aktorService;
    private final PepClient pepClient;

    public BehovsvurderingResource(
            BehovsvurderingService bvService,
            UserService userService,
            AktorService aktorService,
            PepClient pepClient) {

        this.bvService = bvService;
        this.userService = userService;
        this.aktorService = aktorService;
        this.pepClient = pepClient;
    }

    @POST
    @Path("/svar")
    @ApiOperation(value = "Sender inn en behovsvurderings besvarelse")
    public Besvarelse nyttSvar(Svar svar) {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);

        pepClient.sjekkLesetilgangTilAktorId(aktorId.getAktorId());

        return bvService.nyBesvarlse(aktorId, svar);
    }

    @GET
    @Path("/besvarelse")
    @ApiOperation(value = "Henter den siste behovsvurderings besvarelsen på bruker")
    public Besvarelse hentSisteBesvarelse() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);

        pepClient.sjekkLesetilgangTilAktorId(aktorId.getAktorId());

        return bvService.hentBesvarelse(aktorId);

    }
}
