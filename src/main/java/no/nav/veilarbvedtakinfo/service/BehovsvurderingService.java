package no.nav.veilarbvedtakinfo.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BehovsvurderingService {
    private final BehovsvurderingRepository behovsvurderingRepository;

    public Besvarelse nyBesvarlse(AktorId aktorId, Svar svar) {

        if(svar.besvarelseId != null) {
            Besvarelse besvarelse = behovsvurderingRepository.hentBesvarelse(svar.besvarelseId);
            if (!besvarelse.getAktorId().equals(aktorId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        Long besvarsleId = Optional.ofNullable(svar.besvarelseId)
                .orElse(behovsvurderingRepository.lagNyBesvarlse(aktorId));

        behovsvurderingRepository.leggTilNyttSvarPaBesvarelsen(besvarsleId, svar);

        return behovsvurderingRepository.hentBesvarelse(besvarsleId);
    }

    public Besvarelse hentBesvarelse(AktorId aktorId) {
        return behovsvurderingRepository.hentSisteBesvarelse(aktorId);
    }
}
