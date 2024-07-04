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
package eu.openanalytics.phaedra.curvedataservice.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@NoArgsConstructor
public class Curve {

  @Id
  private Long id;
  @Column("substance_name")
  private String substanceName;
  @Column("substance_type")
  private String substanceType;
  @NotNull
  @Column("plate_id")
  private Long plateId;
  @NotNull
  @Column("feature_id")
  private Long featureId;
  @NotNull
  @Column("protocol_id")
  private Long protocolId;
  @NotNull
  @Column("result_set_id")
  private Long resultSetId;
  @NotNull
  @Column("fit_date")
  private Date fitDate;
  private String version;
  private long[] wells;
  @Column("well_concentrations")
  private float[] wellConcentrations;
  @Column("feature_values")
  private float[] featureValues;
  @Column("x_axis_labels")
  private float[] xAxisLabels;
  @Column("plot_dose_data")
  private float[] plotDoseData;
  @Column("plot_prediction_data")
  private float[] plotPredictionData;
  private float[] weights;
  @Column("p_ic50")
  private String pIC50;
  @Column("p_ic50_censor")
  private String pIC50Censor;
  @Column("p_ic50_error")
  private String pIC50StdErr;
  @Column("e_max")
  private Float eMax;
  @Column("e_min")
  private Float eMin;
  @Column("e_max_conc")
  private Float eMaxConc;
  @Column("e_min_conc")
  private Float eMinConc;
  @Column("p_ic20")
  private Float pIC20;
  @Column("p_ic80")
  private Float pIC80;
  private Float slope;
  private Float bottom;
  private Float top;
  @Column("slope_lower_ci")
  private Float slopeLowerCI;
  @Column("slope_upper_ci")
  private Float slopeUpperCI;
  @Column("residual_variance")
  private Float residualVariance;
  private String warning;
}
