package no.nav.fo.veilarbvedtakinfo.mock;

import no.nav.fo.veilarbvedtakinfo.service.UserService;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

public class UserServiceMock extends UserService {

    public UserServiceMock(Provider<HttpServletRequest> requestProvider) {
        super(requestProvider);
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
