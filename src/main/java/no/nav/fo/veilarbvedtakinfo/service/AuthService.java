package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.common.types.identer.NavIdent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;

    @Autowired
    public AuthService(AktorregisterClient aktorregisterClient, Pep pep) {
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
    }

    public boolean erEksternBruker() {
        return AuthContextHolder.erEksternBruker();
    }

    public AktorId hentAktorId(Fnr fnr) {
        return aktorregisterClient.hentAktorId(fnr);
    }

    public NavIdent hentInnloggetVeilederIdent() {
        return AuthContextHolder.getNavIdent()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fant ikke ident til saksbehandler"));
    }

    public String hentInnloggetSubject() {
        return AuthContextHolder.getSubject()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fant ikke subject for bruker"));
    }

    public void sjekkLeseTilgangTilPerson(AktorId aktorId) {
        boolean harTilgang = pep.harVeilederTilgangTilPerson(hentInnloggetVeilederIdent(), ActionId.READ, aktorId);
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}