package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.fo.veilarbvedtakinfo.domain.registrering.*;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;


public class RegistreringClientMock extends RegistreringClient {
    private Date now;

    public RegistreringClientMock() {
        super(null);
        now = Date.from(Instant.now());
    }
    public BrukerRegistreringWrapper hentSisteRegistrering(String fnr) {

        Besvarelse besvarelse = new Besvarelse()
                        .setFremtidigSituasjon(FremtidigSituasjonSvar.NY_ARBEIDSGIVER)
                        .setHelseHinder(HinderSvar.INGEN_SVAR)
                        .setAndreForhold(HinderSvar.JA);

        return new BrukerRegistreringWrapper()
                .setRegistrering(new BrukerRegistrering()
                        .setBesvarelse(besvarelse)
                        .setOpprettetDato(now));

    }
}
