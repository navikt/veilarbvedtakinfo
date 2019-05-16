package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.fo.veilarbvedtakinfo.domain.registrering.*;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;


public class RegistreringClientMock extends RegistreringClient {

    public RegistreringClientMock(Provider<HttpServletRequest> requestProvider) {
        super(requestProvider);
    }
    public BrukerRegistreringWrapper hentSisteRegistrering(String fnr) {

        Besvarelse besvarelse = new Besvarelse()
                        .setFremtidigSituasjon(FremtidigSituasjonSvar.NY_ARBEIDSGIVER);
        BrukerRegistreringWrapper brukerRegistreringWrapper = new BrukerRegistreringWrapper()
                .setRegistrering(new BrukerRegistrering()
                        .setBesvarelse(besvarelse)
                        .setOpprettetDato(Date.from(Instant.now())));

        return brukerRegistreringWrapper;

    }
}
