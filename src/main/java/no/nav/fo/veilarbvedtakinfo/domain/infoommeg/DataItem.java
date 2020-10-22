package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class DataItem {
    ZonedDateTime dato = null;
}
