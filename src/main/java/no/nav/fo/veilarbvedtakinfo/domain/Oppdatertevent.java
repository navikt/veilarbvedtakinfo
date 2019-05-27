package no.nav.fo.veilarbvedtakinfo.domain;

import java.util.Date;

public class Oppdatertevent {
    public final Date tidspunkt;
    public final Oppdaterd oppdaterd;
    public final AktorId aktorId;


    public Oppdatertevent(AktorId aktorId, Date dato, Oppdaterd oppdaterd) {
        this.tidspunkt = dato;
        this.oppdaterd = oppdaterd;
        this.aktorId = aktorId;
    }

    public enum Oppdaterd {
        HELSE_HIDER,
        ANDRE_HINDER,
        FREMTIDIG_SITUASJON
    }
}
