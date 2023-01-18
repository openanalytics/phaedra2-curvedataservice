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
package eu.openanalytics.phaedra.curvedataservice.client.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.curvedataservice.dto.CurveDataDTO;
import eu.openanalytics.phaedra.curvedataservice.client.CurveDataServiceClient;
import eu.openanalytics.phaedra.curvedataservice.client.exception.CurveUnresolvedException;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class CachingHttpCurveDataServiceClient implements CurveDataServiceClient {

    private final HttpCurveDataServiceClient httpResultDataServiceClient;

    private record CurveDataKey(long curveId, long featureId) {};

    private final Cache<CurveDataKey, CurveDataDTO> resultDataCache;
    private final Cache<Long, CurveDataDTO> resultSetCache;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CachingHttpCurveDataServiceClient(PhaedraRestTemplate restTemplate, IAuthorizationService authService) {
        httpResultDataServiceClient = new HttpCurveDataServiceClient(restTemplate, authService);
        resultDataCache = Caffeine.newBuilder()
            .maximumSize(1_000)
            .expireAfterAccess(Duration.ofHours(1))
            .build();
        resultSetCache = Caffeine.newBuilder()
            .maximumSize(1_000)
            .expireAfterAccess(Duration.ofHours(1))
            .build();
    }

    @Override
    public CurveDTO createNewCurve(String substanceName, Long plateId, Long protocolId, Long featureId, Long resultSetId) throws CurveUnresolvedException {
        return httpResultDataServiceClient.createNewCurve(substanceName, plateId, protocolId, featureId, resultSetId);
    }

    @Override
    public CurveDTO createNewCurve(String substanceName, Long plateId, Long protocolId, Long featureId, Long resultSetId, float[] dose, float[] prediction) throws CurveUnresolvedException {
        return httpResultDataServiceClient.createNewCurve(substanceName, plateId, protocolId, featureId, resultSetId, dose, prediction);
    }
}
