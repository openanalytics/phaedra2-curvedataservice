package eu.openanalytics.phaedra.curvedataservice.client.config;

import eu.openanalytics.phaedra.curvedataservice.client.CurveDataServiceClient;
import eu.openanalytics.phaedra.curvedataservice.client.impl.CachingHttpCurveDataServiceClient;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurveDataServiceClientAutoConfiguration {

    @Bean
    public CurveDataServiceClient curveDataServiceClient(PhaedraRestTemplate phaedraRestTemplate,
                                                         IAuthorizationService authorizationService) {
        return new CachingHttpCurveDataServiceClient(phaedraRestTemplate, authorizationService);
    }
}
