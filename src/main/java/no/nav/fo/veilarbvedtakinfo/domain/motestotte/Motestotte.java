package no.nav.fo.veilarbvedtakinfo.domain.motestotte;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Motestotte {
    public Date dato;
}
