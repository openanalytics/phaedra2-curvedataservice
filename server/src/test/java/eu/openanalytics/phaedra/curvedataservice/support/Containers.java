/**
 * Phaedra II
 * <p>
 * Copyright (C) 2016-2024 Open Analytics
 * <p>
 * ===========================================================================
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * Apache License as published by The Apache Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Apache
 * License for more details.
 * <p>
 * You should have received a copy of the Apache License along with this program.  If not, see
 * <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.curvedataservice.support;

import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class Containers {

  @Container
  public static final PostgreSQLContainer<?> postgreSQLContainer;

  static {
    postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
        .withUrlParam("currentSchema", "curvedata");
    postgreSQLContainer.start();
    try {
      var connection = postgreSQLContainer.createConnection("");
      connection.createStatement().executeUpdate("create schema curvedata");
      connection.setSchema("curvedata");

      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
          new ClassLoaderResourceAccessor(), database);
      liquibase.update(new Contexts(), new LabelExpression());
    } catch (SQLException | LiquibaseException e) {
      e.printStackTrace();
    }
  }
}
