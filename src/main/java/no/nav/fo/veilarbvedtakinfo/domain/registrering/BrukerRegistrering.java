package no.nav.fo.veilarbvedtakinfo.domain.registrering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class BrukerRegistrering {

    String manueltRegistrertAv;
    Date opprettetDato;
    Besvarelse besvarelse;

}
