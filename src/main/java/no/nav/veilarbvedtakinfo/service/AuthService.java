package no.nav.veilarbvedtakinfo.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.EksternBrukerId;
import no.nav.common.types.identer.Fnr;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AktorOppslagClient aktorOppslagClient;
    private final AuthContextHolder authContextHolder;

    private final Pep pep;

    public boolean erEksternBruker() {
        return authContextHolder.erEksternBruker();
    }

    public AktorId hentAktorId(Fnr fnr) {
        return aktorOppslagClient.hentAktorId(fnr);
    }

    public String hentInnloggetUid() {
        return authContextHolder.getUid()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fant ikke ident for bruker"));
    }

    public String hentInnloggetBrukerToken() {
        return authContextHolder.getIdTokenString().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Fant ikke token for innlogget bruker"));
    }

    public void sjekkLeseTilgangTilPerson(EksternBrukerId eksternBrukerId) {
        if (authContextHolder.erEksternBruker()) {
            if (!hentInnloggetUid().equals(eksternBrukerId.get())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            return;
        }

        boolean harTilgang = pep.harTilgangTilPerson(hentInnloggetBrukerToken(), ActionId.READ, eksternBrukerId);
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
