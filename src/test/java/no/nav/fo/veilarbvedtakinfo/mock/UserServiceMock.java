package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.common.abac.Pep;
import no.nav.fo.veilarbvedtakinfo.service.UserService;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletRequest;

public class UserServiceMock extends UserService {

    public UserServiceMock(ObjectProvider<HttpServletRequest> requestProvider, Pep pep) {
        super(requestProvider, pep);
    }

    public boolean erEksternBruker() {
        return true;
    }

    public String getFnrFromUrl() {
        return "12345678910";
    }

    public String getUid() {
        return "12345678910"; //TODO: Fjerne fnr
    }


}
