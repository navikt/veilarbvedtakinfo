package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarbvedtakinfo.db.InfoOmMegRepository;
import no.nav.fo.veilarbvedtakinfo.domain.EndretAvType;
import no.nav.fo.veilarbvedtakinfo.domain.infoommeg.*;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistrering;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.BrukerRegistreringWrapper;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.FremtidigSituasjonSvar;
import no.nav.fo.veilarbvedtakinfo.httpclient.RegistreringClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoOmMegService {
    private final InfoOmMegRepository infoOmMegRepository;
    private final RegistreringClient registreringClient;

    @Autowired
    public InfoOmMegService(InfoOmMegRepository infoOmMegRepository, RegistreringClient registreringClient) {
        this.infoOmMegRepository = infoOmMegRepository;
        this.registreringClient = registreringClient;
    }

    public HovedmalData hentFremtidigSituasjon(AktorId aktorId, Fnr fnr) {
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

            return hentNyeste(registrering, hovedmalData);
        }

        return hovedmalData;
    }

    public HovedmalData lagreFremtidigSituasjon(HovedmalData fremtidigSituasjon, AktorId aktorId, String endretAv) {
        long id = infoOmMegRepository.lagreFremtidigSituasjonForAktorId(fremtidigSituasjon, aktorId, endretAv);
        return infoOmMegRepository.hentFremtidigSituasjonForId(id);
    }

    public HelseOgAndreHensynData hentHelseHinder(AktorId aktorId, Fnr fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        return hentHelseHinder(aktorId, registreringWrapper);
    }

    private HelseOgAndreHensynData hentHelseHinder(AktorId aktorId, BrukerRegistreringWrapper registreringWrapper) {
        HelseOgAndreHensynData helseData = infoOmMegRepository.hentHelseHinderForAktorId(aktorId);

        if(registreringWrapper != null) {
            HelseOgAndreHensynData registrering = new HelseOgAndreHensynData()
                    .setDato(registreringWrapper.getRegistrering().getOpprettetDato())
                    .setVerdi(registreringWrapper.getRegistrering().getBesvarelse().getHelseHinder());

            return hentNyeste(registrering, helseData);

        }
        return helseData;
    }

    public HelseOgAndreHensynData lagreHelseHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreHelseHinderForAktorId(helseOgAndreHensynData, aktorId);
        return infoOmMegRepository.hentHelseHinderForId(id);
    }

    public HelseOgAndreHensynData hentAndreHinder(AktorId aktorId, Fnr fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);
        return hentAndreHinder(aktorId, registreringWrapper);
    }

    private HelseOgAndreHensynData hentAndreHinder(AktorId aktorId, BrukerRegistreringWrapper registreringWrapper) {
        HelseOgAndreHensynData andreHinderData = infoOmMegRepository.hentAndreHinderForAktorId(aktorId);

        if(registreringWrapper != null) {
            HelseOgAndreHensynData registrering = new HelseOgAndreHensynData()
                    .setDato(registreringWrapper.getRegistrering().getOpprettetDato())
                    .setVerdi(registreringWrapper.getRegistrering().getBesvarelse().getAndreForhold());

            return hentNyeste(registrering, andreHinderData);

        }
        return andreHinderData;
    }

    public HelseOgAndreHensynData lagreAndreHinder(HelseOgAndreHensynData helseOgAndreHensynData, AktorId aktorId){
        long id = infoOmMegRepository.lagreAndreHinderForAktorId(helseOgAndreHensynData, aktorId);
        return infoOmMegRepository.hentAndreHinderForId(id);
    }

    public InfoOmMegData hentSisteSituasjon(AktorId aktorId, Fnr fnr) {
        BrukerRegistreringWrapper registreringWrapper = registreringClient.hentSisteRegistrering(fnr);

        return new InfoOmMegData()
                .setFremtidigSituasjonData(hentFremtidigSituasjon(aktorId, registreringWrapper))
                .setHelseHinder(hentHelseHinder(aktorId, registreringWrapper))
                .setAndreHinder(hentAndreHinder(aktorId, registreringWrapper));
    }

    public List<HovedmalData> hentSituasjonHistorikk(AktorId aktorId) {
        return infoOmMegRepository.hentSituasjonHistorikk(aktorId);
    }

    private <T extends DataItem> T hentNyeste(T item1, T item2) {
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
