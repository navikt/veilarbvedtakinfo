package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.fo.veilarbvedtakinfo.domain.registrering.*;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;


public class RegistreringClientMock extends RegistreringClient {
    private Date now;

    public RegistreringClientMock(Provider<HttpServletRequest> requestProvider) {
        super(requestProvider);
        now = Date.from(Instant.now());
    }
    public BrukerRegistreringWrapper hentSisteRegistrering(String fnr) {

        Besvarelse besvarelse = new Besvarelse()
                        .setFremtidigSituasjon(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                        .setHelseHinder(HelseHinderSvar.INGEN_SVAR)
                        .setAndreForhold(AndreForholdSvar.JA);
        return new BrukerRegistreringWrapper()
                .setRegistrering(new BrukerRegistrering()
                        .setBesvarelse(besvarelse)
                        .setOpprettetDato(now));

    }
}
