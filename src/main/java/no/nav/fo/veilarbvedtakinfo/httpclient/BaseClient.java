package no.nav.fo.veilarbvedtakinfo.httpclient;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseClient {

    protected final String baseUrl;
    protected final HttpServletRequest httpServletRequestProvider;

    public BaseClient(String baseUrl, HttpServletRequest httpServletRequestProvider) {
        this.baseUrl = baseUrl;
        this.httpServletRequestProvider = httpServletRequestProvider;
    }
}
