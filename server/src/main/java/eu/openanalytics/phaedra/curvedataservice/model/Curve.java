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
    @Column("feature_id")
    private Long featureId;
    @NotNull
    @Column("fit_date")
    private Date fitDate;
    private String version;
    @Column("x_axis_labels")
    private Double[] xAxisLabels;
    @Column("plot_dose_data")
    private Double[] plotDoseData;
    @Column("plot_prediction_data")
    private Double[] plotPredictionData;
}
