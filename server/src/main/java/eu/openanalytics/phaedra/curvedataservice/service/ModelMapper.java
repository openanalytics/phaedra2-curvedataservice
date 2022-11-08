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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

import static java.lang.Float.NaN;
import static java.lang.Float.parseFloat;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

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
        curve.setPIC50(curveDTO.getPIC50());
        curve.setPIC50Censor(curveDTO.getPIC50Censor());
        curve.setPIC50StdErr(curveDTO.getPIC50StdErr());
        curve.setEMax(curveDTO.getEMax());
        curve.setEMin(curveDTO.getEMin());
        curve.setEMaxConc(curveDTO.getEMaxConc());
        curve.setEMinConc(curveDTO.getEMinConc());
        curve.setPIC20(curveDTO.getPIC20());
        curve.setPIC80(curveDTO.getPIC80());
        curve.setSlope(curveDTO.getSlope());
        curve.setBottom(curveDTO.getBottom());
        curve.setTop(curveDTO.getTop());
        curve.setSlopeLowerCI(curveDTO.getSlopeLowerCI());
        curve.setSlopeUpperCI(curveDTO.getSlopeUpperCI());
        curve.setResidualVariance(curveDTO.getResidualVariance());
        curve.setWarning(curveDTO.getWarning());

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
                .pIC50(curve.getPIC50())
                .pIC50StdErr(curve.getPIC50StdErr())
                .eMax(curve.getEMax())
                .eMin(curve.getEMin())
                .eMaxConc(curve.getEMaxConc())
                .eMinConc(curve.getEMinConc())
                .pIC20(curve.getPIC20())
                .pIC80(curve.getPIC80())
                .slope(curve.getSlope())
                .bottom(curve.getBottom())
                .top(curve.getTop())
                .slopeLowerCI(curve.getSlopeLowerCI())
                .slopeUpperCI(curve.getSlopeUpperCI())
                .residualVariance(curve.getResidualVariance())
                .warning(curve.getWarning())
                .build();
    }
}
