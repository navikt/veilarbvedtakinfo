package no.nav.fo.veilarbvedtakinfo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseUtils {

    public static long nesteFraSekvens(JdbcTemplate jdbcTemplate, String sekvensNavn) {
        String sql = "Select " + sekvensNavn + ".nextval from dual";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
