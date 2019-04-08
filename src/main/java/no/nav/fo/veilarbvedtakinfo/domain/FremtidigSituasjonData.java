package no.nav.fo.veilarbvedtakinfo.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FremtidigSituasjonData {
    FremtidigSituasjonSvar alternativId;
    String tekst;
}
