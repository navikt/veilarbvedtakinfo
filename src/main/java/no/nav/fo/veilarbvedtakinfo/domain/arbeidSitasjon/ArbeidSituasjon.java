package no.nav.fo.veilarbvedtakinfo.domain.arbeidSitasjon;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArbeidSituasjon {
    public String opprettet;
    public String endretAvType;
    public String endretAvId;
    public String svarId;
    public String svarTekst;
}
