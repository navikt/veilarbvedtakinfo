package no.nav.veilarbvedtakinfo.domain.registrering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;

@Wither
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BrukerRegistreringWrapper {

    public String type;
    public BrukerRegistrering registrering;

}
