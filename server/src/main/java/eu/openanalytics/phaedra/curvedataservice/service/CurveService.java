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

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.repository.CurveRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CurveDTO createCurve(CurveDTO curveDTO) {
        // workaround for https://github.com/spring-projects/spring-data-jdbc/issues/1033
        var simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("curve").usingGeneratedKeyColumns("id");
        var curve = modelMapper.map(curveDTO);
        Number id = simpleJdbcInsert.executeAndReturnKey(new HashMap<>() {{
            put("substance_name", curve.getSubstanceName());
            put("plate_id", curve.getPlateId());
            put("feature_id", curve.getFeatureId());
            put("protocol_id", curve.getProtocolId());
            put("result_set_id", curve.getResultSetId());
            put("fit_date", curve.getFitDate());
            put("version", curve.getVersion());
            put("wells", curve.getWells());
            put("well_concentrations", curve.getWellConcentrations());
            put("feature_values", curve.getFeatureValues());
            put("x_axis_labels", curve.getXAxisLabels());
            put("plot_dose_data", curve.getPlotDoseData());
            put("plot_prediction_data", curve.getPlotPredictionData());
            put("weights", curve.getWeights());
            put("p_ic50", curve.getPIC50());
            put("p_ic50_censor", curve.getPIC50Censor());
            put("p_ic50_error", curve.getPIC50StdErr());
            put("e_max", curve.getEMax());
            put("e_min", curve.getEMin());
            put("e_max_conc", curve.getEMaxConc());
            put("e_min_conc", curve.getEMinConc());
            put("p_ic20", curve.getPIC20());
            put("p_ic80", curve.getPIC80());
            put("slope", curve.getSlope());
            put("bottom", curve.getBottom());
            put("top", curve.getTop());
            put("slope_lower_ci", curve.getSlopeLowerCI());
            put("slope_upper_ci", curve.getSlopeUpperCI());
            put("residual_variance", curve.getResidualVariance());
            put("warning", curve.getWarning());
        }});

        CurveDTO created = modelMapper.map(curveRepository.findById(id.longValue()).get());
        logger.info("A new curve for " + created.getSubstanceName() + " and featureId " + created.getFeatureId() + " has been created!");
        return created;
    }

    public List<CurveDTO> getCurveByPlateId(Long plateId) {
        List<Curve> curves = curveRepository.findCurveByPlateId(plateId);
        if (CollectionUtils.isNotEmpty(curves))
            return curves.stream().map(c -> modelMapper.map(c)).collect(Collectors.toList());

        return Collections.emptyList();
    }

    public List<CurveDTO> getLatestCurveByPlateId(Long plateId) {
        List<Curve> curves = curveRepository.findLatestCurvesByPlateId(plateId);
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
