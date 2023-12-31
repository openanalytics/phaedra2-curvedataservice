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
package eu.openanalytics.curvedataservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.openanalytics.phaedra.util.dto.validation.OnCreate;
import eu.openanalytics.phaedra.util.dto.validation.OnUpdate;
import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

@Value
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Jackson deserialize compatibility
@NonFinal
public class CurveDTO {
    @Null(groups = OnCreate.class, message = "Id must be null when creating a Curve")
    @Null(groups = OnUpdate.class, message = "Id must be specified in URL and not repeated in body")
    Long id;
    @NotNull(message = "PlateId is mandatory", groups = {OnCreate.class})
    @Null(message = "PlateId cannot be changed", groups = {OnUpdate.class})
    Long plateId;
    @NotNull(message = "ProtocolId is mandatory", groups = {OnCreate.class})
    @Null(message = "ProtocolId cannot be changed", groups = {OnUpdate.class})
    Long protocolId;
    @NotNull(message = "FeatureId is mandatory", groups = {OnCreate.class})
    @Null(message = "FeatureId cannot be changed", groups = {OnUpdate.class})
    Long featureId;
    @NotNull(message = "ResultSetId is mandatory", groups = {OnCreate.class})
    @Null(message = "ResultSetId cannot be changed", groups = {OnUpdate.class})
    Long resultSetId;
    @NotNull(message = "SubstanceName is mandatory", groups = {OnCreate.class})
    @Null(message = "SubstanceName cannot be changed", groups = {OnUpdate.class})
    private String substanceName;
    private String substanceType;
    private Date fitDate;
    private String version;
    private long[] wells;
    private float[] wellConcentrations;
    private float[] featureValues;
    @JsonProperty(value = "xaxisLabels")
    private float[] xAxisLabels;
    private float[] plotDoseData;
    private float[] plotPredictionData;
    private float[] weights;
    private List<CurvePropertyDTO> curveProperties;
    @JsonProperty(value = "pic50")
    private String pIC50;
    @JsonProperty(value = "pic50Censor")
    private String pIC50Censor;
    @JsonProperty(value = "pic50StdErr")
    private String pIC50StdErr;
    @JsonProperty(value = "emax")
    private Float eMax;
    @JsonProperty(value = "emin")
    private Float eMin;
    @JsonProperty(value = "emaxConc")
    private Float eMaxConc;
    @JsonProperty(value = "eminConc")
    private Float eMinConc;
    @JsonProperty(value = "pic20")
    private Float pIC20;
    @JsonProperty(value = "pic80")
    private Float pIC80;
    private Float slope;
    private Float bottom;
    private Float top;
    private Float slopeLowerCI;
    private Float slopeUpperCI;
    private Float residualVariance;
    private String warning;
}
