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

/**
 * With a background validation interval lower than the MySQL {@code wait_timeout},
 * Agroal keeps the idle connections alive, so the {@link MySQLWaitTimeoutEvent} is not triggered.
 */
@QuarkusTest
@TestProfile(BackgroundValidationMySQLTestProfile.class)
public class AgroalBackgroundValidationConnectionPoolTest {

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
        assertEquals(0, events.size());
    }
}
