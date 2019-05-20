package no.nav.fo.veilarbvedtakinfo.service;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;

import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HovedmalData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.HelseOgAndreHensynData;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.InfoOmMegData;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistrering;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.FremtidigSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;

import java.util.List;

public class InfoOmMegService {
    private final InfoOmMegRepository infoOmMegRepository;
    private final RegistreringClient registreringClient;

    public InfoOmMegService(InfoOmMegRepository infoOmMegRepository,
                            RegistreringClient registreringClient) {
        this.infoOmMegRepository = infoOmMegRepository;
        this.registreringClient = registreringClient;
    }

    HovedmalData hentNyesteHovedmal(HovedmalData registrering, HovedmalData hovedmalData) {
        if (registrering == null) {
            return hovedmalData;
        }
        else if (hovedmalData == null) {
            return registrering;
        }
        else if (registrering.getDato().before(hovedmalData.getDato())) {
            return hovedmalData;
        }
        return registrering;
    }

    public HovedmalData hentFremtidigSituasjon(AktorId aktorId, String fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        HovedmalData hovedmalData = infoOmMegRepository.hentFremtidigSituasjonForAktorId(aktorId);

        if (registreringWrapper != null) {
            EndretAvType endretAv = registreringWrapper.getRegistrering().getManueltRegistrertAv() == null? EndretAvType.BRUKER: EndretAvType.VEILEDER;
            BrukerRegistrering brukerRegistrering = registreringWrapper.getRegistrering();
            FremtidigSituasjonSvar fremtidigSituasjon = brukerRegistrering.getBesvarelse().getFremtidigSituasjon();

            HovedmalData registrering = new HovedmalData()
                    .setAlternativId(fremtidigSituasjon)
                    .setDato(brukerRegistrering.getOpprettetDato())
                    .setEndretAv(fremtidigSituasjon == null? EndretAvType.IKKE_SATT: endretAv);

            return hentNyesteHovedmal(registrering, hovedmalData);
        }

        return hovedmalData;
    }

    public List<HovedmalData> hentSituasjonHistorikk(AktorId aktorId) {
        return infoOmMegRepository.hentSituasjonHistorikk(aktorId);
    }

    public HovedmalData lagreFremtidigSituasjon(HovedmalData fremtidigSituasjon, AktorId aktorId, String endretAv) {
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


    public InfoOmMegData hentSisteSituasjon(AktorId aktorId, String fnr) {
        return new InfoOmMegData()
                .setFremtidigSituasjonData(hentFremtidigSituasjon(aktorId, fnr))
                .setHelseHinder(hentHelseHinder(aktorId))
                .setAndreHinder(hentAndreHinder(aktorId));
    }
}
