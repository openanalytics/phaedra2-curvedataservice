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
package eu.openanalytics.phaedra.curvedataservice.api;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.service.CurveService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DoseResponseCurveGraphQLController {

    private final CurveService curveService;

    public DoseResponseCurveGraphQLController(CurveService curveService) {
        this.curveService = curveService;
    }


    @QueryMapping
    public List<CurveDTO> getCurvesByPlateId(@Argument long plateId) {
        return curveService.getLatestCurveByPlateId(plateId);
    }

    @QueryMapping
    public List<CurveDTO> getCurvesBySubstanceName(@Argument String substanceName) {
        return curveService.getCurvesBySubstanceName(substanceName);
    }

    @QueryMapping
    public List<CurveDTO> getCurvesBySubstanceType(@Argument String substanceType) {
        return curveService.getCurvesBySubstanceType(substanceType);
    }

    @QueryMapping
    public List<CurveDTO> getCurvesByFeatureId(@Argument long featureId) {
        return curveService.getCurvesByFeatureId(featureId);
    }

}
