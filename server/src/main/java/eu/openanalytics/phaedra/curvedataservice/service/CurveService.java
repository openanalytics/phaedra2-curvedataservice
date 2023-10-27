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
        Number curveId = simpleJdbcInsert.executeAndReturnKey(new HashMap<>() {{
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
            put("p_ic50", curve.getPIC50()); //TODO: Remove after fully replaced by plate_property table
            put("p_ic50_censor", curve.getPIC50Censor()); //TODO: Remove after fully replaced by plate_property table
            put("p_ic50_error", curve.getPIC50StdErr()); //TODO: Remove after fully replaced by plate_property table
            put("e_max", curve.getEMax()); //TODO: Remove after fully replaced by plate_property table
            put("e_min", curve.getEMin()); //TODO: Remove after fully replaced by plate_property table
            put("e_max_conc", curve.getEMaxConc()); //TODO: Remove after fully replaced by plate_property table
            put("e_min_conc", curve.getEMinConc()); //TODO: Remove after fully replaced by plate_property table
            put("p_ic20", curve.getPIC20()); //TODO: Remove after fully replaced by plate_property table
            put("p_ic80", curve.getPIC80()); //TODO: Remove after fully replaced by plate_property table
            put("slope", curve.getSlope()); //TODO: Remove after fully replaced by plate_property table
            put("bottom", curve.getBottom()); //TODO: Remove after fully replaced by plate_property table
            put("top", curve.getTop()); //TODO: Remove after fully replaced by plate_property table
            put("slope_lower_ci", curve.getSlopeLowerCI()); //TODO: Remove after fully replaced by plate_property table
            put("slope_upper_ci", curve.getSlopeUpperCI()); //TODO: Remove after fully replaced by plate_property table
            put("residual_variance", curve.getResidualVariance()); //TODO: Remove after fully replaced by plate_property table
            put("warning", curve.getWarning()); //TODO: Remove after fully replaced by plate_property table
        }});

        if (curveId != null && CollectionUtils.isNotEmpty(curveDTO.getCurveProperties())) {
            var insertCurveProperty = new SimpleJdbcInsert(dataSource).withTableName("curve_property").usingGeneratedKeyColumns("id");
            curveDTO.getCurveProperties().forEach(curvePropertyDTO -> {
                insertCurveProperty.executeAndReturnKey(new HashMap<>() {{
                    put("curve_id", curveId);
                    put("property_name", curvePropertyDTO.getName());
                    put("property_numeric_value", curvePropertyDTO.getNumericValue());
                    put("property_string_value", curvePropertyDTO.getStringValue());
                }});
            });
        }

        CurveDTO created = modelMapper.map(curveRepository.findById(curveId.longValue()).get());
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

    public List<CurveDTO> getCurvesBySubstanceName(String substanceName) {
        List<Curve> result = curveRepository.findCurvesBySubstanceName(substanceName);
        if (CollectionUtils.isNotEmpty(result))
            return result.stream().map(r -> modelMapper.map(r)).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public List<CurveDTO> getCurvesBySubstanceType(String substanceType) {
        List<Curve> result = curveRepository.findCurvesBySubstanceType(substanceType);
        if (CollectionUtils.isNotEmpty(result))
            return result.stream().map(r -> modelMapper.map(r)).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public List<CurveDTO> getCurvesByFeatureId(long featureId) {
        List<Curve> result = curveRepository.findCurvesByFeatureId(featureId);
        if (CollectionUtils.isNotEmpty(result))
            return result.stream().map(r -> modelMapper.map(r)).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
