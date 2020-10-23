package no.nav.veilarbvedtakinfo.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.types.identer.AktorId;
import no.nav.veilarbvedtakinfo.db.ArbeidSituasjonRepository;
import no.nav.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArbeidSitasjonService {

    private final ArbeidSituasjonRepository repository;

    public void nytSvar(ArbeidSituasjonSvar svar, AktorId aktorId, String avsenderID, boolean erEksternBruker) {
        EndretAvType endretAv = erEksternBruker ? EndretAvType.BRUKER : EndretAvType.VEILEDER;
        repository.lagreSituasjon(aktorId, endretAv, avsenderID, svar);
    }

    public ArbeidSituasjon fetchSvar(AktorId aktorId) {
        return repository.hentSituasjon(aktorId);
    }
}
