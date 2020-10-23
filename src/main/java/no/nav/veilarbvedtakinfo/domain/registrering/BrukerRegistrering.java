package no.nav.veilarbvedtakinfo.domain.registrering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class BrukerRegistrering {

    String manueltRegistrertAv;
    ZonedDateTime opprettetDato;
    Besvarelse besvarelse;
    List<TekstForSporsmal> teksterForBesvarelse;

    public String getSvarTekstForSpmId(String spmId){
         List<TekstForSporsmal> teksterForSpm = teksterForBesvarelse.stream()
                 .filter(tekst -> tekst.getSporsmalId().equals(spmId))
                 .collect(Collectors.toList());
         if (teksterForSpm.isEmpty()) return null;
         return teksterForSpm.get(0).getSvar();
    }
}
