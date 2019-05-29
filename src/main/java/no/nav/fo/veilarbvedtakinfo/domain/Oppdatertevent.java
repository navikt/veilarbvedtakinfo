package no.nav.fo.veilarbvedtakinfo.domain;

import java.util.Date;

public class Oppdatertevent {
    public final Date tidspunkt;
    public final Oppdaterd oppdaterd;
    public final String aktorId;


    public Oppdatertevent(AktorId aktorId, Date dato, Oppdaterd oppdaterd) {
        this.tidspunkt = dato;
        this.oppdaterd = oppdaterd;
        this.aktorId = aktorId.getAktorId();
    }

    public enum Oppdaterd {
        HELSE_HINDER,
        ANDRE_HINDER,
        FREMTIDIG_SITUASJON
    }
}
