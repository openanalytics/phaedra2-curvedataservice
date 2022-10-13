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
package eu.openanalytics.phaedra.curvedataservice.service;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.repository.CurveRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurveService {
    private final ModelMapper modelMapper;
    private final CurveRepository curveRepository;

    private final DataSource dataSource;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CurveDTO createCurve(CurveDTO curveDTO) {
        // workaround for https://github.com/spring-projects/spring-data-jdbc/issues/1033
        var simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("curve").usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(new HashMap<>() {{
            put("substance_name", curveDTO.getSubstanceName());
            put("plate_id", curveDTO.getPlateId());
            put("feature_id", curveDTO.getFeatureId());
            put("protocol_id", curveDTO.getProtocolId());
            put("result_set_id", curveDTO.getResultSetId());
            put("fit_date", curveDTO.getFitDate());
            put("version", curveDTO.getVersion());
            put("x_axis_labels", curveDTO.getXAxisLabels());
            put("plot_dose_data", curveDTO.getPlotDoseData());
            put("plot_prediction_data", curveDTO.getPlotPredictionData());
        }});

        String message = "Dose-Response Curve created for " + curveDTO.getSubstanceName() + " and featureId " + curveDTO.getFeatureId() + " with curveId " + String.valueOf(id.longValue());
        kafkaTemplate.send("curvedata-topic", message);
        return modelMapper.map(curveRepository.findById(id.longValue()).get());
    }

    public List<CurveDTO> getCurveByPlateId(Long plateId) {
        List<Curve> curves = curveRepository.findCurveByPlateId(plateId);
        if (CollectionUtils.isNotEmpty(curves))
            return curves.stream().map(c -> modelMapper.map(c)).collect(Collectors.toList());

        return Collections.emptyList();
    }

    public List<CurveDTO> getAllCurves() {
        List<Curve> curves = (List<Curve>) curveRepository.findAll();
        if (CollectionUtils.isNotEmpty(curves))
            return curves.stream().map(c -> modelMapper.map(c)).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
