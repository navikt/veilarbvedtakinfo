package no.nav.fo.veilarbvedtakinfo.domain.registrering;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class BrukerRegistrering {

    String manueltRegistrertAv;
    Date opprettetDato;
    Besvarelse besvarelse;
    List<TekstForSporsmal> teksterForBesvarelse;

    public String getSvarTekstForSpmId(String spmId){
         List<TekstForSporsmal> teksterForSpm = teksterForBesvarelse.stream()
                 .filter(tekst -> tekst.getSporsmal().equals(spmId))
                 .collect(Collectors.toList());
         if (teksterForSpm.isEmpty()) return null;
         return teksterForSpm.get(0).getSvar();
    }
}
