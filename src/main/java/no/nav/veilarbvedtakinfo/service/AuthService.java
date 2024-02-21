package no.nav.veilarbvedtakinfo.service;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.poao_tilgang.client.Decision;
import no.nav.poao_tilgang.client.EksternBrukerTilgangTilEksternBrukerPolicyInput;
import no.nav.poao_tilgang.client.NavAnsattTilgangTilEksternBrukerPolicyInput;
import no.nav.poao_tilgang.client.PoaoTilgangClient;
import no.nav.poao_tilgang.client.TilgangType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final AktorOppslagClient aktorOppslagClient;
    private final AuthContextHolder authContextHolder;
    private final PoaoTilgangClient poaoTilgangClient;

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

    public boolean isTokenX() {
        return authContextHolder.getIdTokenClaims().map(claims -> claims.getIssuer().contains("tokenx")).orElse(false);
    }

    public void sjekkLeseTilgangTilPerson(Fnr fnr) {
        if (isTokenX()) {
            if (!hentInnloggetUid().equals(fnr.get())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            return;
        }

        if (authContextHolder.erEksternBruker()) {
            harSikkerhetsNivaa4();
            Decision desicion = poaoTilgangClient.evaluatePolicy(new EksternBrukerTilgangTilEksternBrukerPolicyInput(
                    hentInnloggetUid(), fnr.get()
            )).getOrThrow();
            if (desicion.isDeny()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else if (authContextHolder.erInternBruker()) {
            Decision desicion = poaoTilgangClient.evaluatePolicy(new NavAnsattTilgangTilEksternBrukerPolicyInput(
                    hentInnloggetVeilederUUID(), TilgangType.LESE, fnr.get()
            )).getOrThrow();
            if (desicion.isDeny()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            log.error("Systembruker forsøker å autorisere seg, feil om man kommer hit.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private UUID hentInnloggetVeilederUUID() {
        return authContextHolder.getIdTokenClaims()
                .flatMap(claims -> getStringClaimOrEmpty(claims, "oid"))
                .map(UUID::fromString)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Fant ikke oid for innlogget veileder"));
    }

    public static Optional<String> getStringClaimOrEmpty(JWTClaimsSet claims, String claimName) {
        try {
            return ofNullable(claims.getStringClaim(claimName));
        } catch (Exception e) {
            return empty();
        }
    }

    private void harSikkerhetsNivaa4() {
        Optional<String> acrClaim = authContextHolder.getIdTokenClaims()
                .flatMap(claims -> getStringClaimOrEmpty(claims, "acr"));
        if (acrClaim.isEmpty() || !acrClaim.get().equals("Level4")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
