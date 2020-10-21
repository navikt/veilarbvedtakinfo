package no.nav.fo.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.AktorId;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Besvarelse {
    AktorId aktorId;
    long besvarelseId;
    Date sistOppdatert;
    List<Svar> svar;
}
