package no.nav.fo.veilarbvedtakinfo.db;

import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.FremtidigSituasjonData;


public class InfoOmMegRepository {

    public FremtidigSituasjonData hentFremtidigSituasjonForAktorId(AktorId aktorId) {
        return new FremtidigSituasjonData();
    }

    public void lagreFremtidigSituasjonForAktorId(FremtidigSituasjonData fremtidigSituasjonData, AktorId aktorId, String endretAv) {
       return;
    }

}
