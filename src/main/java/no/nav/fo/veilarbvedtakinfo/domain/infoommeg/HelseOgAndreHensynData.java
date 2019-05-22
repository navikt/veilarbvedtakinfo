package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HinderSvar;

import java.util.Date;

@Data
@Accessors(chain = true)
public class HelseOgAndreHensynData extends DataItem {
    HinderSvar verdi;
    Date dato;

}
