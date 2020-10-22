package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.fo.veilarbvedtakinfo.domain.registrering.HinderSvar;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class HelseOgAndreHensynData extends DataItem {
    HinderSvar verdi;
    ZonedDateTime dato;
}
