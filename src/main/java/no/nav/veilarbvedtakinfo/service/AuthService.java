package no.nav.veilarbvedtakinfo.service;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.audit_log.cef.AuthorizationDecision;
import no.nav.common.audit_log.cef.CefMessage;
import no.nav.common.audit_log.cef.CefMessageEvent;
import no.nav.common.audit_log.cef.CefMessageSeverity;
import no.nav.common.audit_log.log.AuditLogger;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.EksternBrukerId;
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
public class AuthService {

    private final AktorOppslagClient aktorOppslagClient;
    private final AuthContextHolder authContextHolder;
	private final PoaoTilgangClient poaoTilgangClient;
	private final UnleashService unleashService;

	private final AuditLogger auditLogger;
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

    public boolean isTokenX() {
        return authContextHolder.getIdTokenClaims().map(claims -> claims.getIssuer().contains("tokendings")).orElse(false);
    }

    public void sjekkLeseTilgangTilPerson(EksternBrukerId eksternBrukerId) {
        if (isTokenX()) {
            if (!hentInnloggetUid().equals(eksternBrukerId.get())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            return;
        }
		if (unleashService.skalBrukePoaoTilgang() && !authContextHolder.erSystemBruker()) {
			if (authContextHolder.erEksternBruker()) {
				harSikkerhetsNivaa4();
				Decision desicion = poaoTilgangClient.evaluatePolicy(new EksternBrukerTilgangTilEksternBrukerPolicyInput(
						hentInnloggetUid(), getEksternBrukerFn(eksternBrukerId)
				)).getOrThrow();

				auditLogWithMessageAndDestinationUserId("Bruker ønsker tilgang til seg selv", getEksternBrukerFn(eksternBrukerId), hentInnloggetUid(), desicion.isPermit() ? AuthorizationDecision.PERMIT : AuthorizationDecision.DENY);
				if (desicion.isDeny()) {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN);
				}
			} else {
				Decision desicion = poaoTilgangClient.evaluatePolicy(new NavAnsattTilgangTilEksternBrukerPolicyInput(
						hentInnloggetVeilederUUID(), TilgangType.LESE, getEksternBrukerFn(eksternBrukerId)
				)).getOrThrow();
				auditLogWithMessageAndDestinationUserId("Veileder tilgang til ekstern bruker", getEksternBrukerFn(eksternBrukerId), hentInnloggetUid(), desicion.isPermit() ? AuthorizationDecision.PERMIT : AuthorizationDecision.DENY);
				if (desicion.isDeny()) {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN);
				}
			}
		} else {
			if (!pep.harTilgangTilPerson(hentInnloggetBrukerToken(), ActionId.READ, eksternBrukerId)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}
    }

	public static Optional<String> getStringClaimOrEmpty(JWTClaimsSet claims, String claimName) {
		try {
			return ofNullable(claims.getStringClaim(claimName));
		} catch (Exception e) {
			return empty();
		}
	}

	private void auditLogWithMessageAndDestinationUserId(String logMessage, String destinationUserId, String sourceUserID, AuthorizationDecision authorizationDecision) {
		auditLogger.log(CefMessage.builder()
				.timeEnded(System.currentTimeMillis())
				.applicationName("veilarbvedtakinfo")
				.sourceUserId(sourceUserID)
				.authorizationDecision(authorizationDecision)
				.event(CefMessageEvent.ACCESS)
				.severity(CefMessageSeverity.INFO)
				.name("veilarbvedtakinfo-audit-log")
				.destinationUserId(destinationUserId)
				.extension("msg", logMessage)
				.build());
	}

	private UUID hentInnloggetVeilederUUID() {
		return authContextHolder.getIdTokenClaims()
				.flatMap(claims -> getStringClaimOrEmpty(claims, "oid"))
				.map(UUID::fromString)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Fant ikke oid for innlogget veileder"));
	}
	private void harSikkerhetsNivaa4() {
		Optional<String> acrClaim = authContextHolder.getIdTokenClaims()
				.flatMap(claims -> getStringClaimOrEmpty(claims, "acr"));
		if (acrClaim.isEmpty() || !acrClaim.get().equals("Level4")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	private String getEksternBrukerFn(EksternBrukerId eksternBrukerId) {
		if (eksternBrukerId.type().equals(EksternBrukerId.Type.AKTOR_ID)) {
			return aktorOppslagClient.hentFnr(AktorId.of(eksternBrukerId.get())).get();
		}
		return eksternBrukerId.get();
	}
}
