package no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArbeidSituasjon {
    public String oprettet;
    public String endretAv;
    public String svarId;
    public String svarTekst;
}
