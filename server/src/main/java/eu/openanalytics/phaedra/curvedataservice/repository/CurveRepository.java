package eu.openanalytics.phaedra.curvedataservice.repository;

import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurveRepository extends CrudRepository<Curve, Long> {
}
