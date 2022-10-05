/**
 * Phaedra II
 *
 * Copyright (C) 2016-2022 Open Analytics
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

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.client.CurveDataServiceClient;
import eu.openanalytics.phaedra.curvedataservice.client.exception.CurveUnresolvedException;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
public class HttpCurveDataServiceClient implements CurveDataServiceClient {

    private final RestTemplate restTemplate;
    private final IAuthorizationService authService;

    public HttpCurveDataServiceClient(PhaedraRestTemplate restTemplate, IAuthorizationService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
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

        HttpEntity<?> httpEntity = new HttpEntity(curveDTO, makeHttpHeaders());
        try {
            var response = restTemplate.postForObject(UrlFactory.curve(), httpEntity, CurveDTO.class);
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
    public CurveDTO createNewCurve(String substanceName, Long plateId, Long protocolId, Long featureId, Long resultSetId, double[] dose, double[] prediction) throws CurveUnresolvedException {
        var curveDTO = CurveDTO.builder()
                .substanceName(substanceName)
                .plateId(plateId)
                .protocolId(protocolId)
                .featureId(featureId)
                .resultSetId(resultSetId)
                .fitDate(new Date())
                .version("0.0.1")
                .plotDoseData(dose)
                .plotPredictionData(dose)
                .build();

        HttpEntity<?> httpEntity = new HttpEntity(curveDTO, makeHttpHeaders());
        try {
            var response = restTemplate.postForObject(UrlFactory.curve(), httpEntity, CurveDTO.class);
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
