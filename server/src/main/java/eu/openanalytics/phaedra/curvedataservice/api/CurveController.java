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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class CurveController {

    private final CurveService curveService;

    public CurveController(CurveService curveService) {
        this.curveService = curveService;
    }

    @PostMapping("/curve")
    public ResponseEntity<CurveDTO> createCurve(@RequestBody CurveDTO curveDTO) {
        CurveDTO result = curveService.createCurve(curveDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/curve")
    public ResponseEntity<List<CurveDTO>> getCurve(@RequestParam Optional<Long> plateId) {
        if (plateId.isPresent())
            return new ResponseEntity<>(curveService.getCurveByPlateId(plateId.get()), HttpStatus.OK);

        return new ResponseEntity<>(curveService.getAllCurves(), HttpStatus.OK);
    }

    @GetMapping("/curve/{plateId}/latest")
    public ResponseEntity<List<CurveDTO>> getCurves(@PathVariable Long plateId) {
        List<CurveDTO> results = curveService.getLatestCurveByPlateId(plateId);
        if (CollectionUtils.isNotEmpty(results))
            return new ResponseEntity<>(results, HttpStatus.OK);

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
