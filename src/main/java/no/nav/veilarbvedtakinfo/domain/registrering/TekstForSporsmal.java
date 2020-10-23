package no.nav.veilarbvedtakinfo.domain.registrering;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class TekstForSporsmal {
    private String sporsmalId;
    private String sporsmal;
    private String svar;
}