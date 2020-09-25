package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.AbacPersonId;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.subject.IdentType;
import no.nav.common.auth.subject.Subject;
import no.nav.common.auth.subject.SubjectHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private Pep pep;
    private ObjectProvider<HttpServletRequest> requestProvider;

    public UserService(ObjectProvider<HttpServletRequest> requestProvider, Pep pep) {
        this.requestProvider = requestProvider;
        this.pep = pep;
    }

    public boolean erEksternBruker() {
        return SubjectHandler.getIdentType()
                .map(identType -> IdentType.EksternBruker == identType)
                .orElse(false);
    }

    public String getUid() {
        return SubjectHandler.getIdent().orElseThrow(IllegalArgumentException::new);
    }

    public String getFnrFromUrl() {
        return requestProvider.getIfAvailable().getParameter("fnr");
    }

    public String hentFnrFraUrlEllerToken() {

        String fnr = getFnrFromUrl();

        if (fnr == null) {
            fnr = getUid();
        }

        return fnr;
    }

    public String getInnloggetVeilederIdent() {
        return SubjectHandler
                .getSubject()
                .filter(subject -> IdentType.InternBruker.equals(subject.getIdentType()))
                .map(Subject::getUid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fant ikke ident for innlogget veileder"));
    }

    public void sjekkLeseTilgangTilPerson(String aktorId) {
        boolean harTilgang = pep.harVeilederTilgangTilPerson(getInnloggetVeilederIdent(), ActionId.READ, AbacPersonId.aktorId(aktorId));
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
