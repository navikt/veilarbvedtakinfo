package no.nav.veilarbvedtakinfo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Component
public class DatabaseUtils {

    public static long nesteFraSekvens(JdbcTemplate jdbcTemplate, String sekvensNavn) {
        String sql = "select " + sekvensNavn + ".nextval from dual";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public static ZonedDateTime hentZonedDateTime(ResultSet rs, String kolonneNavn) throws SQLException {
        return Optional.ofNullable(rs.getTimestamp(kolonneNavn))
                .map(timestamp -> timestamp.toLocalDateTime().atZone(ZoneId.systemDefault()))
                .orElse(null);
    }
}
