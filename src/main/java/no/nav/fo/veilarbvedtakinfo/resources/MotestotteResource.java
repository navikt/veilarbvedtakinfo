package no.nav.fo.veilarbvedtakinfo.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.apiapp.security.PepClient;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.motestotte.Motestotte;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/motestotte")
@Produces("application/json")
@Api(value = "MotestotteResource", description = "Tjenester for deling motestotte")
public class MotestotteResource {
    private final MotestotteRepository msRepo;
    private final UserService userService;
    private final AktorService aktorService;
    private final PepClient pepClient;

    public MotestotteResource(
            MotestotteRepository msRepo,
            UserService userService,
            AktorService aktorService,
            PepClient pepClient) {

        this.msRepo = msRepo;
        this.userService = userService;
        this.aktorService = aktorService;
        this.pepClient = pepClient;
    }

    @POST
    @Path("/")
    @ApiOperation(value = "Sender inn en motestotte besvarelse")
    public void nyttSvar() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);

        pepClient.sjekkLesetilgangTilFnr(fnr);

        msRepo.oppdaterMotestotte(aktorId);
    }

    @GET
    @Path("/")
    @ApiOperation(value = "Henter den siste motestotte på bruker")
    public Motestotte hent() {
        String fnr = userService.hentFnrFraUrlEllerToken();
        AktorId aktorId = userService.getAktorIdOrElseThrow(aktorService, fnr);

        pepClient.sjekkLesetilgangTilFnr(fnr);

        return msRepo.hentMoteStotte(aktorId);

    }
}
