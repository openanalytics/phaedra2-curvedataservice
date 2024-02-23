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
package eu.openanalytics.phaedra.curvedataservice.repository;

import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurveRepository extends CrudRepository<Curve, Long> {
    List<Curve> findCurveByPlateId(Long plateId);

    @Query("select * from curve where plate_id = :plateId order by fit_date desc limit (select count(distinct substance_name) from curve where plate_id = :plateId)")
    List<Curve> findLatestCurvesByPlateId(Long plateId);

    @Query("select * from curve where substance_name = :substanceName order by  fit_date desc")
    List<Curve> findCurvesBySubstanceName(String substanceName);

    @Query("select * from curve where substance_type = :substanceType order by  fit_date desc")
    List<Curve> findCurvesBySubstanceType(String substanceType);

    @Query("select * from curve where feature_id = :featureId order by fit_date desc")
    List<Curve> findCurvesByFeatureId(long featureId);
}
