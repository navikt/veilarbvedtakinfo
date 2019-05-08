package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import static no.nav.common.auth.SubjectHandler.getIdent;
import static no.nav.common.auth.SubjectHandler.getIdentType;

public class UserService {

    private Provider<HttpServletRequest> requestProvider;

    public UserService(Provider<HttpServletRequest> requestProvider) {
        this.requestProvider = requestProvider;
    }

    public boolean erEksternBruker() {
        return getIdentType()
                .map(identType -> IdentType.EksternBruker == identType)
                .orElse(false);
    }

    public String getUid() {
        return getIdent().orElseThrow(IllegalArgumentException::new);
    }

    public String getFnrFromUrl() {
        return requestProvider.get().getParameter("fnr");
    }

    public String hentFnrFraUrlEllerToken() {

        String fnr = getFnrFromUrl();

        if (fnr == null) {
            fnr = getUid();
        }

        return fnr;

    }

    public AktorId getAktorIdOrElseThrow(AktorService aktorService, String fnr) {
        return aktorService.getAktorId(fnr)
                .map(AktorId::new)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke aktør for fnr: " + fnr));
    }

}
