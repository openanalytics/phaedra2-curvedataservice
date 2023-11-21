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
import eu.openanalytics.curvedataservice.dto.CurvePropertyDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.model.CurveProperty;
import eu.openanalytics.phaedra.curvedataservice.repository.CurvePropertyRepository;
import eu.openanalytics.phaedra.curvedataservice.repository.CurveRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CurveService {
    private final ModelMapper modelMapper;
    private final CurveRepository curveRepository;
    private final CurvePropertyRepository curvePropertyRepository;

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
        }});

        if (CollectionUtils.isNotEmpty(curveDTO.getCurveProperties())) {
            curveDTO.getCurveProperties().forEach(curvePropertyDTO -> {
                CurveProperty curveProperty = modelMapper.map(curvePropertyDTO.withCurveId(curveId.longValue()));
                curvePropertyRepository.save(curveProperty);
            });
        }

        List<CurvePropertyDTO> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curveId.longValue())
                .stream().map(modelMapper::map).toList();
        logger.info(String.format("A new curve for %s and featureId %d has been created!", curveDTO.getSubstanceName(), curveDTO.getFeatureId()));
        return curveDTO.withId(curveId.longValue()).withCurveProperties(curveProperties);
    }

    public CurveDTO getCurveById(Long curveId) {
        Optional<Curve> curve = curveRepository.findById(curveId);
        if (curve.isPresent()) {
            List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curveId);
            return modelMapper.map(curve.get())
                    .withCurveProperties(curveProperties.stream()
                            .map(modelMapper::map)
                            .toList());
        }
        return null;
    }

    public List<CurveDTO> getCurveByPlateId(Long plateId) {
        List<Curve> curves = curveRepository.findCurveByPlateId(plateId);
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }
        return Collections.emptyList();
    }

    public List<CurveDTO> getLatestCurveByPlateId(Long plateId) {
        List<Curve> curves = curveRepository.findLatestCurvesByPlateId(plateId);
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }

        return Collections.emptyList();
    }

    public List<CurveDTO> getAllCurves() {
        List<Curve> curves = (List<Curve>) curveRepository.findAll();
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }
        return Collections.emptyList();
    }

    public List<CurveDTO> getCurvesBySubstanceName(String substanceName) {
        List<Curve> curves = curveRepository.findCurvesBySubstanceName(substanceName);
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }
        return Collections.emptyList();
    }

    public List<CurveDTO> getCurvesBySubstanceType(String substanceType) {
        List<Curve> curves = curveRepository.findCurvesBySubstanceType(substanceType);
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }
        return Collections.emptyList();
    }

    public List<CurveDTO> getCurvesByFeatureId(long featureId) {
        List<Curve> curves = curveRepository.findCurvesByFeatureId(featureId);
        if (CollectionUtils.isNotEmpty(curves)) {
            List<CurveDTO> result = new ArrayList<>();
            curves.stream().forEach(curve -> {
                List<CurveProperty> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(curve.getId());
                CurveDTO curveDTO = modelMapper.map(curve)
                        .withCurveProperties(curveProperties.stream()
                                .map(modelMapper::map)
                                .toList());
                result.add(curveDTO);
            });
            return result;
        }
        return Collections.emptyList();
    }
}
