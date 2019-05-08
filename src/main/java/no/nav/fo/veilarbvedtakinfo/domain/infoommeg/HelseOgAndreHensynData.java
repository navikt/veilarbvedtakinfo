package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class HelseOgAndreHensynData {
    boolean verdi;
    Date dato;
}
