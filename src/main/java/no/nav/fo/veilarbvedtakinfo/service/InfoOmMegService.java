package no.nav.fo.veilarbvedtakinfo.service;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;

import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.FremtidigSituasjonData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.InfoOmMegData;

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

    public FremtidigSituasjonData lagreFremtidigSituasjon(FremtidigSituasjonData fremtidigSituasjon, AktorId aktorId, String endretAv) {
        long id = infoOmMegRepository.lagreFremtidigSituasjonForAktorId(fremtidigSituasjon, aktorId, endretAv);

        return infoOmMegRepository.hentFremtidigSituasjonForId(id);

    }

    public HelseOgAndreHensynData hentHelseHinder(AktorId aktorId) {
        return infoOmMegRepository.hentHelseHinderForAktorId(aktorId);
    }

    public HelseOgAndreHensynData hentAndreHinder(AktorId aktorId) {
        return infoOmMegRepository.hentAndreHinderForAktorId(aktorId);
    }

    public HelseOgAndreHensynData lagreHelseHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreHelseHinderForAktorId(helseOgAndreHensynData, aktorId);
        return infoOmMegRepository.hentHelseHinderForId(id);
    }

    public HelseOgAndreHensynData lagreAndreHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreAndreHinderForAktorId(helseOgAndreHensynData, aktorId);
        return infoOmMegRepository.hentAndreHinderForId(id);
    }


    public InfoOmMegData hentSisteSituasjon(AktorId aktorId) {
        return new InfoOmMegData()
                .setFremtidigSituasjonData(hentFremtidigSituasjon(aktorId))
                .setHelseHinder(hentHelseHinder(aktorId))
                .setAndreHinder(hentAndreHinder(aktorId));
    }
}
