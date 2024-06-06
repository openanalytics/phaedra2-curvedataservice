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
package eu.openanalytics.phaedra.curvedataservice.client.impl;

import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.client.CurveDataServiceClient;
import eu.openanalytics.phaedra.curvedataservice.client.exception.CurveUnresolvedException;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;

@Component
public class HttpCurveDataServiceClient implements CurveDataServiceClient {

    private final RestTemplate restTemplate;
    private final IAuthorizationService authService;

    private final UrlFactory urlFactory;
    
    private static final String PROP_BASE_URL = "phaedra.curvedata-service.base-url";
    private static final String DEFAULT_BASE_URL = "http://phaedra-curvedata-service:8080/phaedra/curvedata-service";
    
    public HttpCurveDataServiceClient(PhaedraRestTemplate restTemplate, IAuthorizationService authService, Environment environment) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.urlFactory = new UrlFactory(environment.getProperty(PROP_BASE_URL, DEFAULT_BASE_URL));
    }

    @Override
    public CurveDTO createNewCurve(String substanceName, Long plateId, Long protocolId, Long featureId, Long resultSetId) throws CurveUnresolvedException {
        var curveDTO = CurveDTO.builder()
                .substanceName(substanceName)
                .plateId(plateId)
                .protocolId(protocolId)
                .featureId(featureId)
                .resultSetId(resultSetId)
                .fitDate(new Date())
                .version("0.0.1")
                .build();

        HttpEntity<?> httpEntity = new HttpEntity<>(curveDTO, makeHttpHeaders());
        try {
            var response = restTemplate.postForObject(urlFactory.curve(), httpEntity, CurveDTO.class);
            if (response == null) {
                throw new CurveUnresolvedException("Curve could not be converted");
            }
            return response;
        } catch (HttpClientErrorException ex) {
            throw new CurveUnresolvedException("Error while creating Curve", ex);
        } catch (HttpServerErrorException ex) {
            throw new CurveUnresolvedException("Server Error while creating Curve", ex);
        }
    }

    @Override
    public CurveDTO createNewCurve(String substanceName, Long plateId, Long protocolId, Long featureId, Long resultSetId, float[] dose, float[] prediction) throws CurveUnresolvedException {
        var curveDTO = CurveDTO.builder()
                .substanceName(substanceName)
                .plateId(plateId)
                .protocolId(protocolId)
                .featureId(featureId)
                .resultSetId(resultSetId)
                .fitDate(new Date())
                .version("0.0.1")
                .plotDoseData(dose)
                .plotPredictionData(prediction)
                .build();

        HttpEntity<?> httpEntity = new HttpEntity<>(curveDTO, makeHttpHeaders());
        try {
            var response = restTemplate.postForObject(urlFactory.curve(), httpEntity, CurveDTO.class);
            if (response == null) {
                throw new CurveUnresolvedException("Curve could not be converted");
            }
            return response;
        } catch (HttpClientErrorException ex) {
            throw new CurveUnresolvedException("Error while creating Curve", ex);
        } catch (HttpServerErrorException ex) {
            throw new CurveUnresolvedException("Server Error while creating Curve", ex);
        }
    }

    private HttpHeaders makeHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String bearerToken = authService.getCurrentBearerToken();
        if (bearerToken != null) httpHeaders.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken));
        return httpHeaders;
    }
}
