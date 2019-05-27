package no.nav.fo.veilarbvedtakinfo.config;

import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;

import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class KafkaConfig {

    public static final String KAFKA_TOPIC = "privat-fo-brukerInfoEndret-q0";
    private static final String KAFKA_BROKERS_URL_PROPERTY = "KAFKA_BROKERS_URL";

    private static final String KAFKA_BROKERS = "10.184.160.37:8443";
    private static final String USERNAME = getRequiredProperty(StsSecurityConstants.SYSTEMUSER_USERNAME);
    private static final String PASSWORD = getRequiredProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD);


    private static Properties producerConfig() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + USERNAME + "\" password=\"" + PASSWORD + "\";");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafkatest-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public static KafkaTemplate kafkaProducer() {
        return new KafkaTemplate(new DefaultKafkaProducerFactory(producerConfig()));
    }
}
