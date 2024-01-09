/**
 * Phaedra II
 *
 * Copyright (C) 2016-2024 Open Analytics
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

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.support.Containers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@Testcontainers
@Sql({"/jdbc/initial.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
class KafkaConsumerServiceTest {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @MockBean
    private CurveService curveService;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    void testOnCreateCurveMessage() {
        CurveDTO curveDTO = CurveDTO.builder()
                .plateId(1L)
                .protocolId(2L)
                .featureId(3L)
                .resultSetId(4L)
                .substanceName("test substance")
                .build();

        kafkaConsumerService.onCreateCurveMessage(curveDTO, "saveCurve");

        verify(curveService, times(1)).createCurve(curveDTO);
    }

    @Test
    void testOnCreateCurveMessage_withWrongEventKey() {
        CurveDTO curveDTO = CurveDTO.builder()
                .plateId(1L)
                .protocolId(2L)
                .featureId(3L)
                .resultSetId(4L)
                .substanceName("test substance")
                .build();

        kafkaConsumerService.onCreateCurveMessage(curveDTO, "wrongEventKey");

        verifyNoInteractions(curveService);
    }
}

