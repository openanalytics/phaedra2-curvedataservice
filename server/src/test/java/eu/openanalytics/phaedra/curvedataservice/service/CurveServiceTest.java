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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.support.Containers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/initial.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CurveServiceTest {
    @Autowired
    private CurveService curveService;
    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void createCurve() throws JsonProcessingException {
        String curveData = "{\"id\":null,\"plateId\":187,\"protocolId\":127,\"featureId\":249,\"resultSetId\":21747,\"substanceName\":\"G1980649-1\",\"fitDate\":1666951924819,\"version\":\"0.0.1\",\"wells\":[40943,40944,40945,40946,40947,40948,40949,40950],\"wellConcentrations\":[4.519,5.121,5.723,6.325,6.927,7.529,8.131,8.733],\"featureValues\":[73.490326,64.98485,29.564001,-52.41315,-62.2616,-9.102355,-47.43297,-60.079273],\"plotDoseData\":[10.405382,10.503393,10.601404,10.699415,10.797426,10.895437,10.993448,11.091459,11.18947,11.287481,11.385492,11.483503,11.581514,11.679525,11.777536,11.875547,11.973558,12.071569,12.16958,12.267591,12.3656025,12.463614,12.5616255,12.6596365,12.7576475,12.855659,12.95367,13.051681,13.149692,13.247703,13.345714,13.443725,13.541736,13.639747,13.737758,13.835769,13.93378,14.031791,14.129802,14.227813,14.325824,14.423835,14.521846,14.619857,14.717868,14.815879,14.91389,15.011901,15.109912,15.207923,15.305934,15.403945,15.501956,15.599968,15.697979,15.79599,15.894001,15.992012,16.090023,16.188034,16.286045,16.384056,16.482067,16.580078,16.67809,16.7761,16.874111,16.972122,17.070133,17.168144,17.266155,17.364166,17.462177,17.560188,17.6582,17.75621,17.854221,17.952232,18.050243,18.148254,18.246265,18.344276,18.442287,18.540298,18.63831,18.73632,18.834332,18.932343,19.030354,19.128365,19.226376,19.324387,19.422398,19.520409,19.61842,19.71643,19.814442,19.912453,20.010464,20.108475],\"plotPredictionData\":[69.23459,69.23459,69.23459,69.23459,69.23459,69.23459,69.23458,69.23457,69.23456,69.23453,69.23446,69.234314,69.23402,69.2334,69.23209,69.22938,69.2237,69.211845,69.18709,69.135414,69.02772,68.80379,68.34061,67.39276,65.49495,61.85575,55.421974,45.52838,33.160835,21.154861,12.044367,6.3400025,3.1867557,1.5628297,0.7569496,0.36438757,0.17489237,0.08382184,0.04014629,0.019221654,0.009201691,0.004404653,0.0021083374,0.0010091623,4.830347E-4,2.3120323E-4,1.1066459E-4,5.2969153E-5,2.5353458E-5,1.2135321E-5,5.808518E-6,2.7802214E-6,1.3307405E-6,6.36953E-7,3.048747E-7,1.4592692E-7,6.984727E-8,3.343208E-8,1.6002115E-8,7.659342E-9,3.6661094E-9,1.7547669E-9,8.399113E-10,4.020198E-10,1.9242498E-10,9.210335E-11,4.4084857E-11,2.110102E-11,1.0099909E-11,4.834276E-12,2.3139047E-12,1.1075402E-12,5.3011913E-13,2.5373914E-13,1.2145111E-13,5.813203E-14,2.7824636E-14,1.3318138E-14,6.374667E-15,3.0512058E-15,1.4604461E-15,6.9903596E-16,3.3459043E-16,1.6015021E-16,7.6655185E-17,3.669066E-17,1.756182E-17,8.405887E-18,4.02344E-18,1.9258016E-18,9.217764E-19,4.4120413E-19,2.1118039E-19,1.01080547E-19,4.838175E-20,2.3157708E-20,1.1084333E-20,5.3054666E-21,2.5394378E-21,1.2154906E-21],\"weights\":[0.9990607,0.9990645,1.0,0.8625574,0.8090102,0.9957065,0.8866892,0.8215159],\"slope\":7.517469,\"bottom\":0.0,\"top\":69.23459,\"slopeLowerCI\":-343.8883,\"slopeUpperCI\":358.92325,\"residualVariance\":1780.8633,\"warning\":\"The covariance matrix is positive definite.\",\"emax\":NaN,\"emin\":-62.2616,\"emaxConc\":10.405382,\"eminConc\":15.9500065,\"pic20\":5.7860622,\"pic80\":5.625886,\"xaxisLabels\":null,\"pic50\":\"5.706\",\"pic50Censor\":null,\"pic50StdErr\":\"0.346890690773892\"}";
        CurveDTO curveDTO = objectMapper.readValue(curveData, CurveDTO.class);
        assertThat(curveDTO).isNotNull();

        CurveDTO created = curveService.createCurve(curveDTO);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
    }

    @Test
    public void getAllCurves() throws IOException {
        String path = "src/test/resources/json/curvedata.json";
        File file = new File(path);

        List<CurveDTO> curveDTOList = objectMapper.readValue(file, new TypeReference<List<CurveDTO>>() {});
        assertThat(curveDTOList).hasSize(17);

        for (CurveDTO curveDTO: curveDTOList) {
            CurveDTO created = curveService.createCurve(curveDTO);
            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
        }

        List<CurveDTO> result = curveService.getAllCurves();
        assertThat(result).hasSize(17);
    }
}
