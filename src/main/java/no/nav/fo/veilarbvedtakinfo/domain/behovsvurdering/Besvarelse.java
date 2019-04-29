package no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Besvarelse {
    public long besvarelseId;
    public Date sistOppdatert;
    public List<Svar> svar;
}
