package eu.openanalytics.phaedra.curvedataservice.service;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.model.Curve;
import eu.openanalytics.phaedra.curvedataservice.repository.CurveRepository;
import org.springframework.stereotype.Service;

@Service
public class CurveService {
    private final ModelMapper modelMapper;
    private final CurveRepository curveRepository;

    public CurveService(ModelMapper modelMapper, CurveRepository curveRepository) {
        this.modelMapper = modelMapper;
        this.curveRepository = curveRepository;
    }

    public CurveDTO createCurve(CurveDTO curveDTO) {
        Curve savedCurve = curveRepository.save(modelMapper.map(curveDTO));
        return modelMapper.map(savedCurve);
    }
}
