package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.fo.veilarbvedtakinfo.db.ArbeidSituasjonRepository;
import no.nav.fo.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.fo.veilarbvedtakinfo.db.MotestotteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BehovsvurderingRepository.class,
        MotestotteRepository.class,
        ArbeidSituasjonRepository.class
})
public class RepositoryTestConfig {}
