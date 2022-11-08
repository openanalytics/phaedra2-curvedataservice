package eu.openanalytics.phaedra.curvedataservice.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/initial.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CurveControllerTest {
}
