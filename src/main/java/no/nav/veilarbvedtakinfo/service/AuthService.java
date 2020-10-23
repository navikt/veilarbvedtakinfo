package no.nav.veilarbvedtakinfo.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AktorregisterClient aktorregisterClient;

    private final Pep pep;

    public boolean erEksternBruker() {
        return AuthContextHolder.erEksternBruker();
    }

    public AktorId hentAktorId(Fnr fnr) {
        return aktorregisterClient.hentAktorId(fnr);
    }

    public String hentInnloggetSubject() {
        return AuthContextHolder.getSubject()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fant ikke subject for bruker"));
    }

    public String hentInnloggetBrukerToken() {
        return AuthContextHolder.getIdTokenString().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Fant ikke token for innlogget bruker"));
    }

    public void sjekkLeseTilgangTilPerson(AktorId aktorId) {
        boolean harTilgang = pep.harTilgangTilPerson(hentInnloggetBrukerToken(), ActionId.READ, aktorId);
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
