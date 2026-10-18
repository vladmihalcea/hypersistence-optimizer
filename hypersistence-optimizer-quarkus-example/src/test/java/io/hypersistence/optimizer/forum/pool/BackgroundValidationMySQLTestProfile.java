package io.hypersistence.optimizer.forum.pool;

import java.util.Map;

/**
 * The {@link BackgroundValidationMySQLTestProfile} validates the idle Agroal connections every 30 seconds,
 * so before the MySQL {@code wait_timeout} expires.
 */
public class BackgroundValidationMySQLTestProfile extends MySQLTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> properties = super.getConfigOverrides();
        properties.put("quarkus.datasource.jdbc.background-validation-interval", "30S");
        return properties;
    }
}
