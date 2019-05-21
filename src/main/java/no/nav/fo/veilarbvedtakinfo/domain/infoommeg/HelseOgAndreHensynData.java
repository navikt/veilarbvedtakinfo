package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HelseHinderSvar;

import java.util.Date;

@Data
@Accessors(chain = true)
public class HelseOgAndreHensynData extends DataItem {
    HelseHinderSvar verdi;
    Date dato;
}
