package eu.openanalytics.phaedra.curvedataservice.service;

import static eu.openanalytics.phaedra.curvedataservice.config.KafkaConfig.EVENT_SAVE_CURVE;
import static eu.openanalytics.phaedra.curvedataservice.config.KafkaConfig.GROUP_ID;
import static eu.openanalytics.phaedra.curvedataservice.config.KafkaConfig.TOPIC_CURVEDATA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import eu.openanalytics.curvedataservice.dto.CurveDTO;

@Service
public class KafkaConsumerService {

    @Autowired
    CurveService curveService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = TOPIC_CURVEDATA, groupId = GROUP_ID)
    public void onCreateCurveMessage(CurveDTO curveDTO, @Header(KafkaHeaders.RECEIVED_KEY) String msgKey) {
        if (!EVENT_SAVE_CURVE.equalsIgnoreCase(msgKey)) return;
        logger.info("Create new curve for " + curveDTO.getSubstanceName() + " and featureId " + curveDTO.getFeatureId());
        curveService.createCurve(curveDTO);
    }
}
