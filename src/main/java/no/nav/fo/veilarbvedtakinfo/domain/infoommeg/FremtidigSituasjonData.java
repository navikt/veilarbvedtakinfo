package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class FremtidigSituasjonData {
    FremtidigSituasjonSvar alternativId;
    String tekst;
    String endretAv;
    Date dato;

    public FremtidigSituasjonData setEndretAv(String endretAv) {
        this.endretAv =  isNumeric(endretAv)? "BRUKER": "VEILEDER";
        return this;
    }

    private boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
