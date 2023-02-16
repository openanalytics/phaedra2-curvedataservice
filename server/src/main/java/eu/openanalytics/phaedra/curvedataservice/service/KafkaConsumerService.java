package eu.openanalytics.phaedra.curvedataservice.service;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.config.KafkaConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    CurveService curveService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = KafkaConsumerConfig.CURVEDATA_TOPIC, groupId = "curvedata-service", filter="saveCurveDataEventFilter")
    public void onCreateCurveMessage(CurveDTO curveDTO, @Header(KafkaHeaders.RECEIVED_KEY) String msgKey) {
        if (msgKey.equals(KafkaConsumerConfig.SAVE_CURVE_EVENT)) {
            logger.info("Create new curve for " + curveDTO.getSubstanceName() + " and featureId " + curveDTO.getFeatureId());
            curveService.createCurve(curveDTO);
        }
    }
}
