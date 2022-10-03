package eu.openanalytics.phaedra.curvedataservice.api;

import eu.openanalytics.curvedataservice.dto.CurveDTO;
import eu.openanalytics.phaedra.curvedataservice.service.CurveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurveController {

    private final CurveService curveService;

    public CurveController(CurveService curveService) {
        this.curveService = curveService;
    }

    @PostMapping("/curve")
    public ResponseEntity<CurveDTO> createCurve(@RequestBody CurveDTO curveDTO) {
        CurveDTO result = curveService.createCurve(curveDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
