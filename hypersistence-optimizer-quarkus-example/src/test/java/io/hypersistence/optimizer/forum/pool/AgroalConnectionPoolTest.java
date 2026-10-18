package io.hypersistence.optimizer.forum.pool;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.jdbc.event.server.MySQLWaitTimeoutEvent;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Quarkus uses the Agroal connection pool, which, by default, validates the idle connections every 2 minutes,
 * so the MySQL server closes the connections that stay idle for more than the 60 seconds {@code wait_timeout}.
 */
@QuarkusTest
@TestProfile(MySQLTestProfile.class)
public class AgroalConnectionPoolTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void test() {
        HypersistenceOptimizer hypersistenceOptimizer = new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
        );

        List<MySQLWaitTimeoutEvent> events = new ArrayList<>();
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event instanceof MySQLWaitTimeoutEvent) {
                events.add((MySQLWaitTimeoutEvent) event);
            }
        }
        assertEquals(1, events.size());

        MySQLWaitTimeoutEvent event = events.get(0);
        LOGGER.info("The {} connection pool was detected: {}", event.getConnectionPool(), event.getDescription());
        assertEquals("Agroal", event.getConnectionPool());
        assertEquals(MySQLTestProfile.WAIT_TIMEOUT_SECONDS, event.getWaitTimeoutSeconds());
        assertTrue(event.getDescription().contains("quarkus.datasource.jdbc.background-validation-interval"));
    }
}
