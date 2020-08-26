package no.nav.fo.veilarbvedtakinfo.mock;


import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.AbacPersonId;
import no.nav.common.abac.domain.request.ActionId;

public class PepClientMock implements Pep {

    public PepClientMock() {
      super();
    }

    @Override
    public boolean harVeilederTilgangTilEnhet(String s, String s1) {
        return true;
    }

    @Override
    public boolean harVeilederTilgangTilPerson(String s, ActionId actionId, AbacPersonId abacPersonId) {
        return true;
    }

    @Override
    public boolean harTilgangTilPerson(String s, ActionId actionId, AbacPersonId abacPersonId) {
        return true;
    }

    @Override
    public boolean harVeilederTilgangTilOppfolging(String s) {
        return true;
    }

    @Override
    public boolean harVeilederTilgangTilModia(String s) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilKode6(String s) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilKode7(String s) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilEgenAnsatt(String s) {
        return false;
    }

    @Override
    public AbacClient getAbacClient() {
        return null;
    }
}
