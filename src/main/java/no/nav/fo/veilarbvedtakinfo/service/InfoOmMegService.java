package no.nav.fo.veilarbvedtakinfo.service;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;

import no.nav.fo.veilarbvedtakinfo.domain.Oppdatertevent;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.*;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistrering;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.FremtidigSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;

import java.util.List;

public class InfoOmMegService {
    private final InfoOmMegRepository infoOmMegRepository;
    private final RegistreringClient registreringClient;
    private final OppdatertService oppdatertService;

    public InfoOmMegService(InfoOmMegRepository infoOmMegRepository, RegistreringClient registreringClient, OppdatertService oppdatertService) {
        this.infoOmMegRepository = infoOmMegRepository;
        this.registreringClient = registreringClient;
        this.oppdatertService = oppdatertService;
    }

    public HovedmalData hentFremtidigSituasjon(AktorId aktorId, String fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        return hentFremtidigSituasjon(aktorId, registreringWrapper);
    }

    private HovedmalData hentFremtidigSituasjon(AktorId aktorId, BrukerRegistreringWrapper registreringWrapper) {
        HovedmalData hovedmalData = infoOmMegRepository.hentFremtidigSituasjonForAktorId(aktorId);

        if (registreringWrapper != null) {
            EndretAvType endretAv = registreringWrapper.getRegistrering().getManueltRegistrertAv() == null? EndretAvType.BRUKER: EndretAvType.VEILEDER;
            BrukerRegistrering brukerRegistrering = registreringWrapper.getRegistrering();
            FremtidigSituasjonSvar fremtidigSituasjon = brukerRegistrering.getBesvarelse().getFremtidigSituasjon();

            HovedmalData registrering = new HovedmalData()
                    .setAlternativId(fremtidigSituasjon)
                    .setDato(brukerRegistrering.getOpprettetDato())
                    .setEndretAv(fremtidigSituasjon == null? EndretAvType.IKKE_SATT: endretAv);

            if(registrering.getAlternativId() != HovedmalSvar.IKKE_OPPGITT) {
                registrering.setTekst(brukerRegistrering.getSvarTekstForSpmId("fremtidigSituasjon"));
            }

            return (HovedmalData) hentNyeste(registrering, hovedmalData);
        }

        return hovedmalData;
    }

    public HovedmalData lagreFremtidigSituasjon(HovedmalData fremtidigSituasjon, AktorId aktorId, String endretAv) {
        long id = infoOmMegRepository.lagreFremtidigSituasjonForAktorId(fremtidigSituasjon, aktorId, endretAv);
        HovedmalData hovedmalData = infoOmMegRepository.hentFremtidigSituasjonForId(id);

        oppdatertService.sendOppdatert(aktorId, hovedmalData.getDato(), Oppdatertevent.Oppdaterd.FREMTIDIG_SITUASJON);
        return hovedmalData;

    }

    public HelseOgAndreHensynData hentHelseHinder(AktorId aktorId, String fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        return hentHelseHinder(aktorId, registreringWrapper);
    }

    private HelseOgAndreHensynData hentHelseHinder(AktorId aktorId, BrukerRegistreringWrapper registreringWrapper) {
        HelseOgAndreHensynData helseData = infoOmMegRepository.hentHelseHinderForAktorId(aktorId);

        if(registreringWrapper != null) {
            HelseOgAndreHensynData registrering = new HelseOgAndreHensynData()
                    .setDato(registreringWrapper.getRegistrering().getOpprettetDato())
                    .setVerdi(registreringWrapper.getRegistrering().getBesvarelse().getHelseHinder());

            return (HelseOgAndreHensynData) hentNyeste(registrering, helseData);

        }
        return helseData;
    }

    public HelseOgAndreHensynData lagreHelseHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreHelseHinderForAktorId(helseOgAndreHensynData, aktorId);
        HelseOgAndreHensynData hensyn = infoOmMegRepository.hentHelseHinderForId(id);
        oppdatertService.sendOppdatert(aktorId, hensyn.getDato(), Oppdatertevent.Oppdaterd.HELSE_HIDER);
        return hensyn;
    }

    public HelseOgAndreHensynData hentAndreHinder(AktorId aktorId, String fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        return hentAndreHinder(aktorId, registreringWrapper);
    }

    private HelseOgAndreHensynData hentAndreHinder(AktorId aktorId, BrukerRegistreringWrapper registreringWrapper) {
        HelseOgAndreHensynData andreHinderData = infoOmMegRepository.hentAndreHinderForAktorId(aktorId);

        if(registreringWrapper != null) {
            HelseOgAndreHensynData registrering = new HelseOgAndreHensynData()
                    .setDato(registreringWrapper.getRegistrering().getOpprettetDato())
                    .setVerdi(registreringWrapper.getRegistrering().getBesvarelse().getAndreForhold());

            return (HelseOgAndreHensynData) hentNyeste(registrering, andreHinderData);

        }
        return andreHinderData;
    }

    public HelseOgAndreHensynData lagreAndreHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreAndreHinderForAktorId(helseOgAndreHensynData, aktorId);
        HelseOgAndreHensynData hensyn = infoOmMegRepository.hentAndreHinderForId(id);
        oppdatertService.sendOppdatert(aktorId, hensyn.getDato(), Oppdatertevent.Oppdaterd.ANDRE_HINDER);
        return hensyn;
    }

    public InfoOmMegData hentSisteSituasjon(AktorId aktorId, String fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);

        return new InfoOmMegData()
                .setFremtidigSituasjonData(hentFremtidigSituasjon(aktorId, registreringWrapper))
                .setHelseHinder(hentHelseHinder(aktorId, registreringWrapper))
                .setAndreHinder(hentAndreHinder(aktorId, registreringWrapper));
    }

    public List<HovedmalData> hentSituasjonHistorikk(AktorId aktorId) {
        return infoOmMegRepository.hentSituasjonHistorikk(aktorId);
    }

    private DataItem hentNyeste(DataItem item1, DataItem item2) {
        if (item1 == null) {
            return item2;
        }
        else if (item2 == null) {
            return item1;
        }
        else if (item1.getDato().before(item2.getDato())) {
            return item2;
        }
        return item1;
    }
}
