package eu.openanalytics.phaedra.curvedataservice.support;

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

import java.sql.SQLException;

public class Containers {

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
                .withUrlParam("currentSchema","curvedata");

        postgreSQLContainer.start();
        try {
            var connection = postgreSQLContainer.createConnection("");
            connection.createStatement().executeUpdate("create schema curvedata");
            connection.setSchema("curvedata");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
