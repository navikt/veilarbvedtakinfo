package no.nav.fo.veilarbvedtakinfo.test;

import no.nav.common.auth.context.AuthContext;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.UserRole;
import no.nav.common.test.auth.AuthTestUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

import static no.nav.common.auth.Constants.AAD_NAV_IDENT_CLAIM;

public class TestAuthContextFilter implements Filter {

    private final AuthContext authContext;

    public TestAuthContextFilter(AuthContext authContext) {
        this.authContext = authContext;
    }

    public TestAuthContextFilter(UserRole role, String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);

        if (role == UserRole.INTERN) {
            claims.put(AAD_NAV_IDENT_CLAIM, "Z123456");
        }

        this.authContext = AuthTestUtils.createAuthContext(role, claims);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        AuthContextHolder.withContext(authContext, () -> filterChain.doFilter(servletRequest, servletResponse));
    }

}
