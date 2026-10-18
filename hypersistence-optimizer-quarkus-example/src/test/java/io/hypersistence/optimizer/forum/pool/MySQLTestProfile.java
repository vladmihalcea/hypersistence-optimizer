package io.hypersistence.optimizer.forum.pool;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link MySQLTestProfile} runs the application on MySQL, with the {@code wait_timeout} lowered to 60 seconds.
 * <p>
 * To run the tests using this profile, you need a MySQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public class MySQLTestProfile implements QuarkusTestProfile {

    public static final int WAIT_TIMEOUT_SECONDS = 60;

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.datasource.db-kind", "mysql");
        properties.put(
            "quarkus.datasource.jdbc.url",
            "jdbc:mysql://localhost:3306/high_performance_java_persistence?sessionVariables=wait_timeout=" + WAIT_TIMEOUT_SECONDS
        );
        properties.put("quarkus.datasource.username", "mysql");
        properties.put("quarkus.datasource.password", "admin");
        properties.put("quarkus.hibernate-orm.database.generation", "create-drop");
        return properties;
    }
}
