package eu.openanalytics.phaedra.curvedataservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    public static final String CURVEDATA_TOPIC = "curvedata-topic";
    public static final String SAVE_CURVE_EVENT = "createCurve";

    @Bean
    public RecordFilterStrategy<String, Object> saveCurveDataEventFilter() {
        RecordFilterStrategy<String, Object> recordFilterStrategy = consumerRecord -> !(consumerRecord.key().equalsIgnoreCase(SAVE_CURVE_EVENT));
        return recordFilterStrategy;
    }
}
