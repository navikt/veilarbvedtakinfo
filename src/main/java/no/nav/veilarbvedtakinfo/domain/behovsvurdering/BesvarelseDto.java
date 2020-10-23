package no.nav.veilarbvedtakinfo.domain.behovsvurdering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class BesvarelseDto {
    public long besvarelseId;
    public ZonedDateTime sistOppdatert;
    public List<Svar> svar;

    public static BesvarelseDto fromBesvarelse(Besvarelse besvarelse){
        return new BesvarelseDto()
                .setBesvarelseId(besvarelse.getBesvarelseId())
                .setSistOppdatert(besvarelse.getSistOppdatert())
                .setSvar(besvarelse.getSvar());
    }
}
