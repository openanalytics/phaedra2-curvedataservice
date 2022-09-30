package eu.openanalytics.phaedra.curvedataservice.repository;

import eu.openanalytics.phaedra.curvedataservice.model.CurveData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurveDataRepository extends CrudRepository <CurveData, Long>{
}
