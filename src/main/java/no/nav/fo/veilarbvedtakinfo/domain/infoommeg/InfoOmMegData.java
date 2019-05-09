package no.nav.fo.veilarbvedtakinfo.domain.infoommeg;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InfoOmMegData {
    HelseOgAndreHensynData helseHinder;
    HelseOgAndreHensynData andreHinder;
    FremtidigSituasjonData fremtidigSituasjonData;
}
