package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.common.types.identer.AktorId;
import no.nav.fo.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BehovsvurderingService {
    private final BehovsvurderingRepository behovsvurderingRepository;

    public BehovsvurderingService(BehovsvurderingRepository behovsvurderingRepository) {
        this.behovsvurderingRepository = behovsvurderingRepository;
    }

    public Besvarelse nyBesvarlse(AktorId aktorId, Svar svar) {
        Long besvarsleId = Optional.ofNullable(svar.besvarelseId)
                .orElse(behovsvurderingRepository.lagNyBesvarlse(aktorId));
        behovsvurderingRepository.leggTilNyttSvarPaBesvarelsen(besvarsleId, svar);
        return hentBesvarelse(besvarsleId);
    }

    public Besvarelse hentBesvarelse(Long besvarlseId) {
        return behovsvurderingRepository.hentBesvarelse(besvarlseId);
    }

    public Besvarelse hentBesvarelse(AktorId aktorId) {
        return behovsvurderingRepository.hentSisteBesvarelse(aktorId);
    }
}
