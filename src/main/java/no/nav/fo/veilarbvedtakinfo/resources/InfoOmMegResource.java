package no.nav.fo.veilarbvedtakinfo.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.fo.veilarbvedtakinfo.domain.FremtidigSituasjonData;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;



@Component
@Path("/")
@Produces("application/json")
@Api(value = "InfoOmMegResource", description = "Tjenester for deling av arbeidssøkerstatus.")

public class InfoOmMegResource {

    @GET
    @Path("/fremtidigsituasjon")
    @ApiOperation(value = "Henter nyeste verdi for fremtidig situasjon.")
    public FremtidigSituasjonData hentFremtidigSituasjon() {
        return new FremtidigSituasjonData();
    }

}
