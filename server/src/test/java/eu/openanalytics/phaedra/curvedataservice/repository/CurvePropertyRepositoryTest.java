package eu.openanalytics.phaedra.curvedataservice.repository;

import eu.openanalytics.phaedra.curvedataservice.support.Containers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/initial.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CurvePropertyRepositoryTest {

  @Autowired
  private CurvePropertyRepository curvePropertyRepository;

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
    registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
    registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
  }

  @Test
  public void contextLoads() {
    Assertions.assertThat(curvePropertyRepository).isNotNull();
  }

}
