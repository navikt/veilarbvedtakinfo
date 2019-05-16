package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.FremtidigSituasjonSvar;

import java.util.Date;

@Data
@Accessors(chain = true)
public class HovedmalData {
    HovedmalSvar alternativId;
    String tekst;
    EndretAvType endretAv;
    Date dato;

    public HovedmalData setEndretAv(String endretAv) {
        this.endretAv =  isNumeric(endretAv)? EndretAvType.BRUKER: EndretAvType.VEILEDER;
        return this;
    }

    public HovedmalData setEndretAv(EndretAvType endretAv) {
        this.endretAv = endretAv;
        return this;
    }
    //TODO: ikke mappe direkte ref FO-2178
    public HovedmalData setAlternativId(FremtidigSituasjonSvar fremtidigSituasjonSvar) {
        if (fremtidigSituasjonSvar == null) alternativId = HovedmalSvar.IKKE_OPPGITT;
        else alternativId = HovedmalSvar.valueOf(fremtidigSituasjonSvar.name());
        return this;
    }
    public HovedmalData setAlternativId(HovedmalSvar hovedmalSvar) {
        alternativId = hovedmalSvar;
        return this;
    }

    private boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
