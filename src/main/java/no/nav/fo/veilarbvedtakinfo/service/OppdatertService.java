package no.nav.fo.veilarbvedtakinfo.service;

import no.nav.fo.veilarbvedtakinfo.config.KafkaConfig;
import no.nav.fo.veilarbvedtakinfo.domain.AktorId;
import no.nav.fo.veilarbvedtakinfo.domain.Oppdatertevent;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;

public class OppdatertService {
    private KafkaTemplate kafkaTemplate;

    public OppdatertService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOppdatert(AktorId aktorId, Date dato, Oppdatertevent.Oppdaterd opdatert) {
        kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC, aktorId, new Oppdatertevent(aktorId, dato, opdatert));
    }
}
