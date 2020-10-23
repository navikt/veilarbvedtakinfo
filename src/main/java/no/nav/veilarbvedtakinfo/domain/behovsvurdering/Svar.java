package no.nav.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class Svar {
    public String spmId;
    public Long besvarelseId;
    public String svar;
    public String spm;
    public ZonedDateTime dato;
}
