package no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Svar {
    public String spmId;
    public Long besvarelseId;
    public String svar;
    public String spm;
    public Date dato;
}
