package no.nav.veilarbvedtakinfo.domain.registrering;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class Besvarelse {
    private String utdanning;
    private String utdanningBestatt;
    private String utdanningGodkjent;
    private HinderSvar helseHinder;
    private HinderSvar andreForhold;
    private String sisteStilling;
    private String dinSituasjon;
    private FremtidigSituasjonSvar fremtidigSituasjon;
    private String tilbakeIArbeid;
}