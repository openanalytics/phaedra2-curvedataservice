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

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.curvedataservice.dto.CurvePropertyDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.model.CurveProperty;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Service;

@Service
public class ModelMapper {
    private final org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();

    public ModelMapper() {
        Configuration builderConfiguration = modelMapper.getConfiguration().copy()
                .setDestinationNameTransformer(NameTransformers.builder())
                .setDestinationNamingConvention(NamingConventions.builder());

    }

    /**
     * Maps a {@link eu.openanalytics.curvedataservice.dto.CurveDTO} to a {@link Curve}.
     */
    public Curve map(CurveDTO curveDTO) {
        Curve curve = new Curve();
        curve.setId(curveDTO.getId());
        curve.setSubstanceName(curveDTO.getSubstanceName());
        curve.setSubstanceType(curveDTO.getSubstanceType());
        curve.setPlateId(curveDTO.getPlateId());
        curve.setFeatureId(curveDTO.getFeatureId());
        curve.setProtocolId(curveDTO.getProtocolId());
        curve.setResultSetId(curveDTO.getResultSetId());
        curve.setFitDate(curveDTO.getFitDate());
        curve.setVersion(curveDTO.getVersion());
        curve.setXAxisLabels(curveDTO.getXAxisLabels());
        curve.setPlotDoseData(curveDTO.getPlotDoseData());
        curve.setPlotPredictionData(curveDTO.getPlotPredictionData());
        curve.setWells(curveDTO.getWells());
        curve.setFeatureValues(curveDTO.getFeatureValues());
        curve.setWellConcentrations(curveDTO.getWellConcentrations());
        curve.setWeights(curveDTO.getWeights());
        return curve;
    }

    /**
     * Maps a {@link Curve} to a {@link CurveDTO}.
     */
    public CurveDTO map(Curve curve) {
        return CurveDTO.builder()
                .id(curve.getId())
                .substanceName(curve.getSubstanceName())
                .substanceType(curve.getSubstanceType())
                .plateId(curve.getPlateId())
                .featureId(curve.getFeatureId())
                .protocolId(curve.getProtocolId())
                .resultSetId(curve.getResultSetId())
                .fitDate(curve.getFitDate())
                .version(curve.getVersion())
                .xAxisLabels(curve.getXAxisLabels())
                .plotDoseData(curve.getPlotDoseData())
                .plotPredictionData(curve.getPlotPredictionData())
                .wells(curve.getWells())
                .featureValues(curve.getFeatureValues())
                .wellConcentrations(curve.getWellConcentrations())
                .weights(curve.getWeights())
                .build();
    }

    /**
     * Maps a {@link CurvePropertyDTO} to a {@link CurveProperty}.
     */
    public CurveProperty map(CurvePropertyDTO curvePropertyDTO) {
        CurveProperty curveProperty = new CurveProperty();
        curveProperty.setCurveId(curvePropertyDTO.getCurveId());
        curveProperty.setName(curvePropertyDTO.getName());
        curveProperty.setNumericValue(curvePropertyDTO.getNumericValue());
        curveProperty.setStringValue(curvePropertyDTO.getStringValue());
        return curveProperty;
    }

    /**
     * Maps a {@link CurveProperty} to a {@link CurvePropertyDTO}.
     */
    public CurvePropertyDTO map(CurveProperty curveProperty) {
        return CurvePropertyDTO.builder()
                .curveId(curveProperty.getCurveId())
                .name(curveProperty.getName())
                .numericValue(curveProperty.getNumericValue())
                .stringValue(curveProperty.getStringValue())
                .build();
    }
}
