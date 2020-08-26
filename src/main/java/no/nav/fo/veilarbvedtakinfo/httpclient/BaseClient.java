package no.nav.fo.veilarbvedtakinfo.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseClient {

    protected final String baseUrl;
    protected final HttpServletRequest httpServletRequestProvider;
    protected static final int HTTP_READ_TIMEOUT = 120000;

    public BaseClient(String baseUrl, HttpServletRequest httpServletRequestProvider) {
        this.baseUrl = baseUrl;
        this.httpServletRequestProvider = httpServletRequestProvider;
    }
}
