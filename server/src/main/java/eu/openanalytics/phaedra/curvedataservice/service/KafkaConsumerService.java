/**
 * Phaedra II
 *
 * Copyright (C) 2016-2023 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
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
