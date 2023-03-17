package eu.openanalytics.phaedra.curvedataservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	public static final String GROUP_ID = "curvedata-service";
	
    public static final String TOPIC_CURVEDATA = "curvedata";
    
    public static final String EVENT_SAVE_CURVE = "saveCurve";

}
