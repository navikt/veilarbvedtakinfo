package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.fo.veilarbvedtakinfo.db.ArbeidSitasjonRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;
import org.springframework.stereotype.Service;

@Service
public class ArbeidSitasjonService {
    private final ArbeidSitasjonRepository repository;

    public ArbeidSitasjonService(ArbeidSitasjonRepository repository) {
        this.repository = repository;
    }

    public void nytSvar(ArbeidSituasjonSvar svar, String aktorId, String avsenderID, boolean erEksternBruker) {
        EndretAvType endretAv = erEksternBruker ? EndretAvType.BRUKER : EndretAvType.VEILEDER;
        repository.lagreSitasjon(aktorId, endretAv, avsenderID, svar);
    }

    public ArbeidSituasjon fetchSvar(String aktorId) {
        return repository.hentSituasjon(aktorId);
    }
}
