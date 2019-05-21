package no.nav.fo.veilarbvedtakinfo.httpclient;

import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;

import javax.ws.rs.core.Response;
import static javax.ws.rs.core.HttpHeaders.COOKIE;
import static no.nav.sbl.rest.RestUtils.RestConfig.builder;
import static no.nav.sbl.rest.RestUtils.withClient;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Slf4j
public class RegistreringClient extends BaseClient {

    public static final String REGISTRERING_API_PROPERTY_NAME = "veilarbregistreringSBS_URL";

    @Inject
    public RegistreringClient(Provider<HttpServletRequest> httpServletRequestProvider) {
        super(getRequiredProperty(REGISTRERING_API_PROPERTY_NAME), httpServletRequestProvider);
    }

    public BrukerRegistreringWrapper hentSisteRegistrering(String fnr) {
        String cookies = httpServletRequestProvider.get().getHeader(COOKIE);
        try {
            return withClient(builder().readTimeout(HTTP_READ_TIMEOUT).build(),
                    c -> c.target(baseUrl + "/registrering?fnr=" + fnr)
                            .request()
                            .header(COOKIE, cookies)
                            .get(BrukerRegistreringWrapper.class));
        } catch (ForbiddenException e) {
            log.error("Ingen tilgang " + e);
            Response response = e.getResponse();
            throw new WebApplicationException(response);
        } catch (Exception e) {
            log.error("Feil ved kall til tjeneste " + e);
            throw new InternalServerErrorException();
        }
    }
}
