package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.fo.veilarbvedtakinfo.db.ArbeidSitasjonRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjon;
import no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon.ArbeidSituasjonSvar;

public class ArbeidSitasjonService {
    private final ArbeidSitasjonRepository repository;

    public ArbeidSitasjonService(ArbeidSitasjonRepository repository) {
        this.repository = repository;
    }

    public void nytSvar(ArbeidSituasjonSvar svar, AktorId aktorId, String avsenderID, boolean erEksternBruker) {
        EndretAvType endretAv = erEksternBruker ? EndretAvType.BRUKER : EndretAvType.VEILEDER;
        repository.lagreSitasjon(aktorId, endretAv, avsenderID, svar);
    }

    public ArbeidSituasjon fetchSvar(AktorId aktorId) {
        return repository.hentSituasjon(aktorId);
    }
}
