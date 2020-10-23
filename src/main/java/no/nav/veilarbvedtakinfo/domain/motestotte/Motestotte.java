package no.nav.veilarbvedtakinfo.domain.motestotte;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class Motestotte {
    public ZonedDateTime dato;
}
