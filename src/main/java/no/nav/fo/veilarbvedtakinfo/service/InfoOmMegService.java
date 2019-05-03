package no.nav.fo.veilarbvedtakinfo.service;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.FremtidigSituasjonData;

import java.util.List;


public class InfoOmMegService {
    private final InfoOmMegRepository infoOmMegRepository;

    public InfoOmMegService(InfoOmMegRepository infoOmMegRepository) {
        this.infoOmMegRepository = infoOmMegRepository;
    }

    public FremtidigSituasjonData hentFremtidigSituasjon(AktorId aktorId) {
        return infoOmMegRepository.hentFremtidigSituasjonForAktorId(aktorId);
    }

    public List<FremtidigSituasjonData> hentSituasjonHistorikk(AktorId aktorId) {
        return infoOmMegRepository.hentSituasjonHistorikk(aktorId);
    }

    public void lagreFremtidigSituasjon(FremtidigSituasjonData fremtidigSituasjon, AktorId aktorId, String endretAv) {
        infoOmMegRepository.lagreFremtidigSituasjonForAktorId(fremtidigSituasjon, aktorId, endretAv);

    }
}
