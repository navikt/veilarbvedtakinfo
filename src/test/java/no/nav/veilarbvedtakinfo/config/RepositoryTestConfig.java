package no.nav.veilarbvedtakinfo.config;

import no.nav.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.veilarbvedtakinfo.db.MotestotteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BehovsvurderingRepository.class,
        MotestotteRepository.class,
})
public class RepositoryTestConfig {}
