/**
 * Phaedra II
 * <p>
 * Copyright (C) 2016-2024 Open Analytics
 * <p>
 * ===========================================================================
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * Apache License as published by The Apache Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Apache
 * License for more details.
 * <p>
 * You should have received a copy of the Apache License along with this program.  If not, see
 * <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.curvedataservice.service;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.curvedataservice.dto.CurvePropertyDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.model.CurveProperty;
import eu.openanalytics.phaedra.curvedataservice.repository.CurvePropertyRepository;
import eu.openanalytics.phaedra.curvedataservice.repository.CurveRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurveService {

  private final ModelMapper modelMapper;
  private final CurveRepository curveRepository;
  private final CurvePropertyRepository curvePropertyRepository;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public CurveDTO createCurve(CurveDTO curveDTO) {
    Curve curve = modelMapper.map(curveDTO);
    Curve created = curveRepository.save(curve);

    if (CollectionUtils.isNotEmpty(curveDTO.getCurveProperties())) {
      curveDTO.getCurveProperties().forEach(curvePropertyDTO -> {
        CurveProperty curveProperty = modelMapper.map(
            curvePropertyDTO.withCurveId(created.getId()));
        curvePropertyRepository.save(curveProperty);
      });
    }

    List<CurvePropertyDTO> curveProperties = curvePropertyRepository.findCurvePropertyByCurveId(
            created.getId())
        .stream().map(modelMapper::map).toList();
    logger.info(String.format("A new curve for %s and featureId %d has been created!",
        curveDTO.getSubstanceName(), curveDTO.getFeatureId()));
    return curveDTO.withId(created.getId()).withCurveProperties(curveProperties);
  }

  public CurveDTO getCurveById(Long curveId) {
    return curveRepository.findById(curveId)
        .map(this::toCurveDTOWithProperties)
        .orElse(null);
  }

  public List<CurveDTO> getCurveByPlateId(Long plateId) {
    return curveRepository.findCurveByPlateId(plateId).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  public List<CurveDTO> getLatestCurveByPlateId(Long plateId) {
    return curveRepository.findLatestCurvesByPlateId(plateId).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  public List<CurveDTO> getAllCurves() {
    return ((List<Curve>) curveRepository.findAll()).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  public List<CurveDTO> getCurvesBySubstanceName(String substanceName) {
    return curveRepository.findCurvesBySubstanceName(substanceName).stream()
        .map(this::toCurveDTOWithProperties).
        toList();
  }

  public List<CurveDTO> getCurvesBySubstanceType(String substanceType) {
    return curveRepository.findCurvesBySubstanceType(substanceType).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  public List<CurveDTO> getCurvesByFeatureId(long featureId) {
    return curveRepository.findCurvesByFeatureId(featureId).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  public List<CurveDTO> getCurvesThatIncludesWellId(long wellId) {
    return curveRepository.findCurvesThatIncludesWellId(wellId).stream()
        .map(this::toCurveDTOWithProperties)
        .toList();
  }

  private CurveDTO toCurveDTOWithProperties(Curve curve) {
    List<CurveProperty> curveProperties = curvePropertyRepository
        .findCurvePropertyByCurveId(curve.getId());
    return modelMapper.map(curve)
        .withCurveProperties(curveProperties.stream()
            .map(modelMapper::map)
            .toList());
  }
}
