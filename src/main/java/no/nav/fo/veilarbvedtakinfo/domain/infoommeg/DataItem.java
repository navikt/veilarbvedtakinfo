package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class DataItem {
    Date dato = null;
}
