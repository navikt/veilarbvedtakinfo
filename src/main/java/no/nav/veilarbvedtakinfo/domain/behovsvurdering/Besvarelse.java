package no.nav.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.AktorId;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class Besvarelse {
    AktorId aktorId;
    long besvarelseId;
    ZonedDateTime sistOppdatert;
    List<Svar> svar;
}
