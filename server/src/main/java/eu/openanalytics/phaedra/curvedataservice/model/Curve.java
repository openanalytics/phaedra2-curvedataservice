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
package eu.openanalytics.phaedra.curvedataservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data()
@NoArgsConstructor
public class Curve {
    @Id
    private Long id;
    @Column("substance_name")
    private String substanceName;
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
    @Column("x_axis_labels")
    private float[] xAxisLabels;
    @Column("plot_dose_data")
    private float[] plotDoseData;
    @Column("plot_prediction_data")
    private float[] plotPredictionData;
}
