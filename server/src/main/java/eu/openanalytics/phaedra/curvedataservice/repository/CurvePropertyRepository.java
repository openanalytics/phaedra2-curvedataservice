package eu.openanalytics.phaedra.curvedataservice.repository;

import eu.openanalytics.phaedra.curvedataservice.model.CurveProperty;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurvePropertyRepository extends CrudRepository<CurveProperty, Long> {

  @Query("select * from curve_property where curve_id = :curveId")
  List<CurveProperty> findCurvePropertyByCurveId(Long curveId);
}
