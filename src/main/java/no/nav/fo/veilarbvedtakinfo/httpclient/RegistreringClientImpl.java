package no.nav.fo.veilarbvedtakinfo.httpclient;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.rest.client.RestClient;
import no.nav.common.rest.client.RestUtils;
import no.nav.common.sts.SystemUserTokenProvider;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;

import static java.lang.String.format;
import static javax.ws.rs.core.HttpHeaders.COOKIE;
import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;
import static no.nav.common.utils.UrlUtils.joinPaths;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class RegistreringClientImpl implements RegistreringClient {

    public static final String VEILARBREGISTRERING_URL_PROPERTY_NAME = "VEILARBREGISTRERING_URL";

    private final SystemUserTokenProvider systemUserTokenProvider;

    private final HttpServletRequest httpServletRequest;

    private final OkHttpClient client;

    private final String baseUrl;

    public RegistreringClientImpl(HttpServletRequest httpServletRequest, SystemUserTokenProvider systemUserTokenProvider) {
        this.httpServletRequest = httpServletRequest;
        this.systemUserTokenProvider = systemUserTokenProvider;
        this.client = RestClient.baseClient();
        this.baseUrl = getRequiredProperty(VEILARBREGISTRERING_URL_PROPERTY_NAME);
    }

    @Override
    public BrukerRegistreringWrapper hentSisteRegistrering(Fnr fnr) {
        String cookies = httpServletRequest.getHeader(COOKIE);

        Request request = new Request.Builder()
                .url(joinPaths(baseUrl, format("/veilarbregistrering/api/registrering?fnr=%s", fnr.get())))
                .header(COOKIE, cookies)
                .header(AUTHORIZATION, "Bearer " + systemUserTokenProvider.getSystemUserToken())
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, BrukerRegistreringWrapper.class);
        } catch (Exception e) {
            log.error("Feil ved kall til tjeneste " + e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(joinPaths(baseUrl, "/api/ping"), client);
    }
}

